package tgb.cryptoexchange.tgcommon.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.exception.HandlerTypeNotFoundException;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;
import tgb.cryptoexchange.tgcommon.handler.*;
import tgb.cryptoexchange.tgcommon.service.RedisUserStateService;
import tgb.cryptoexchange.tgcommon.service.sender.ResponseSender;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Слушатель ивентов с телеграм-апдейтами.<br>
 * Распределяет апдейты среди фильтров и хендлеров.
 */
@Service
@Slf4j
public class TelegramUpdateEventListener {

    private final ConcurrentHashMap<Long, ReentrantLock> userLocks = new ConcurrentHashMap<>();

    private final RedisUserStateService redisUserStateService;

    private final Map<UpdateType, UpdateHandler> updateHandlers;

    private final Map<UserState, StateHandler> stateHandlerMap;

    private final List<UpdateFilter> updateFilters;

    private final EmptyHandler emptyHandler;

    private final AntiSpam antiSpam;

    private final BannedCache bannedCache;

    private final ResponseSender responseSender;

    public TelegramUpdateEventListener(RedisUserStateService redisUserStateService, List<UpdateHandler> updateHandlers,
                                       List<StateHandler> stateHandlers, EmptyHandler emptyHandler,
                                       List<UpdateFilter> updateFilters, ObjectProvider<AntiSpam> antiSpam,
                                       ObjectProvider<BannedCache> bannedCache, ResponseSender responseSender) {
        this.redisUserStateService = redisUserStateService;
        this.emptyHandler = emptyHandler;
        this.antiSpam = antiSpam.getIfAvailable();
        if (Objects.isNull(this.antiSpam)) {
            throw new TelegramCommonException("Отсутствует реализация интерфейса AntiSpam");
        }
        this.bannedCache = bannedCache.getIfAvailable();
        if (Objects.isNull(this.bannedCache)) {
            throw new TelegramCommonException("Отсутствует реализация интерфейса BannedCache");
        }
        this.responseSender = responseSender;
        log.debug("Загрузка обработчиков апдейтов.");
        this.updateHandlers = new EnumMap<>(UpdateType.class);
        for (UpdateHandler updateHandler : updateHandlers) {
            UpdateType updateType = updateHandler.getUpdateType();
            if (Objects.isNull(updateType)) {
                throw new HandlerTypeNotFoundException("UpdateType null для " + updateHandler.getClass().getName());
            }
            log.debug("Добавлен обработчик апдейтов типа {}", updateHandler.getUpdateType().name());
            this.updateHandlers.put(updateHandler.getUpdateType(), updateHandler);
        }
        this.stateHandlerMap = new HashMap<>();
        for (StateHandler stateHandler : stateHandlers) {
            UserState userState = stateHandler.getUserState();
            if (Objects.isNull(userState)) {
                throw new HandlerTypeNotFoundException("UserState null для " + stateHandler.getClass().getName());
            }
            stateHandlerMap.put(stateHandler.getUserState(), stateHandler);
        }
        this.updateFilters = updateFilters;
        log.debug("Загружено {} обработчиков апдейтов.", updateHandlers.size());
    }

    private ReentrantLock getLock(Long chatId) {
        return userLocks.computeIfAbsent(chatId, k -> new ReentrantLock());
    }

    /**
     * Метод обработки нового телеграм-апдейта. Все апдейты обрабатываются синхронно для каждого пользователя.
     * Во избежания спама, следующий апдейт пользователя будет обработан только после того, как был обработан предыдущий.<br>
     * Порядок обработки:
     * 1. Проверка находится ли пользователь в бане.
     * Если пользователь в бане, то дальнейшная обработка апдейта прекращается.<br>
     * 2. Если пользователь не в бане, то проверка пользователя на спам.
     * Если апдейт является спамом, то дальнейшная обработка апдейта прекращается.<br>
     * 3. Если апдейт не является спамом, то выполняется поиск подходящего фильтра.
     * Если найден подходящий фильтр, то обрабокта апдейта передается фильтру.<br>
     * 4. Если фильтр не найден, то выполняется поиск обработчика состояния пользователя ({@link UserState}).
     * Если в Redis было сохранено состояние за пользователем автором апдейта, то обработка передается обработчику данного состояния.<br>
     * 5. Если сохраненного за пользователем состояния не найдено, определяется тип данного апдейта ({@link UpdateType})
     * и выполняется поиск обработчика для данного типа, после чего найденному обработчику передается обработка апдейта.<br>
     * 6. Если обработчик найден не был, получается объект сообщения из реализации
     * {@link EmptyHandler#getEmptyMessage(Long)} и выполняется его отправка.
     * @param event ивент с телеграм-апдейтом
     */
    @EventListener
    @Async
    public void update(TelegramUpdateEvent event) {
        Update update = event.getUpdate();
        log.trace("Получен апдейт: {}", event.getUpdate());
        Long chatId = UpdateType.getChatId(update);

        ReentrantLock lock = getLock(chatId);
        boolean lockAcquired = false;
        try {
            if (lock.tryLock()) {
                lockAcquired = true;
                if (preHandle(update, chatId)) {
                    return;
                }
                UpdateType updateType = UpdateType.fromUpdate(update);
                if (handleState(update, updateType, chatId))
                    return;
                if (!handle(update, updateType)) {
                    Chat chat = UpdateType.getChat(update);
                    if (Objects.nonNull(chat) && Boolean.TRUE.equals(chat.isUserChat())) {
                        responseSender.execute(emptyHandler.getEmptyMessage(UpdateType.getChatId(update)));
                    }
                }
            }
        } catch (Exception e) {
            Long time = System.currentTimeMillis();
            log.error("{} Необработанная ошибка.", time, e);
            responseSender.sendMessage(chatId,
                    "Произошла ошибка." + System.lineSeparator() + time + System.lineSeparator()
                            + "Введите /start для выхода в главное меню."
            );
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
        }
    }

    private boolean preHandle(Update update, Long chatId) {
        if (bannedCache.get(chatId))
            return true;
        if (antiSpam.isSpam(chatId))
            return true;
        return handleFilter(update);
    }

    private boolean handle(Update update, UpdateType updateType) {
        UpdateHandler updateHandler = updateHandlers.get(updateType);
        if (updateHandler != null) {
            return updateHandler.handle(update);
        }
        return false;
    }

    private boolean handleState(Update update, UpdateType updateType, Long chatId) {
        if (UpdateType.STATE_UPDATE_TYPES.contains(updateType)) {
            UserState userState = redisUserStateService.get(chatId);
            if (Objects.nonNull(userState)) {
                StateHandler stateHandler = stateHandlerMap.get(userState);
                if (Objects.nonNull(stateHandler)) {
                    stateHandler.handle(update);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean handleFilter(Update update) {
        for (UpdateFilter updateFilter : updateFilters) {
            if (updateFilter.match(update)) {
                updateFilter.handle(update);
                return true;
            }
        }
        return false;
    }

}
