package tgb.cryptoexchange.tgcommon.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.exception.HandlerTypeNotFoundException;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;
import tgb.cryptoexchange.tgcommon.handler.*;
import tgb.cryptoexchange.tgcommon.service.RedisUserStateService;
import tgb.cryptoexchange.tgcommon.service.sender.ResponseSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateEventListenerTest {

    @Mock
    private RedisUserStateService redisUserStateService;

    @Mock
    private EmptyHandler emptyHandler;

    @Mock
    private ObjectProvider<AntiSpam> antiSpamProvider;

    @Mock
    private ObjectProvider<BannedCache> bannedCacheProvider;

    @Mock
    private ResponseSender responseSender;

    @Mock
    private AntiSpam antiSpam;

    @Mock
    private BannedCache bannedCache;

    @Test
    @DisplayName("new TelegramUpdateEventListener - отсутствует antiSpam - проброшен TelegramCommonException")
    void shouldThrowTelegramCommonExceptionIfAntiSpamIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(null);
        Supplier<TelegramUpdateEventListener> constructorSupplier = () ->
                new TelegramUpdateEventListener(
                        redisUserStateService,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        emptyHandler,
                        new ArrayList<>(),
                        antiSpamProvider,
                        bannedCacheProvider,
                        responseSender
                );
        assertThrows(TelegramCommonException.class, constructorSupplier::get);
    }


    @Test
    @DisplayName("new TelegramUpdateEventListener - отсутствует antiSpam - проброшен TelegramCommonException")
    void shouldThrowTelegramCommonExceptionIfBannedCacheIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(null);
        Supplier<TelegramUpdateEventListener> constructorSupplier = () -> new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        assertThrows(TelegramCommonException.class, constructorSupplier::get);
    }

    @Test
    @DisplayName("new TelegramUpdateEventListener - один из updateType == null - проброшен HandlerTypeNotFoundException")
    void shouldThrowTelegramCommonExceptionIfUpdateTypeOfUpdateHandlerIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);

        List<UpdateHandler> mockedUpdateHandlers = new ArrayList<>();
        UpdateHandler updateHandlerWithNullUpdateType = Mockito.mock(UpdateHandler.class);
        when(updateHandlerWithNullUpdateType.getUpdateType()).thenReturn(null);
        UpdateHandler updateHandlerWithNonNullUpdateType = Mockito.mock(UpdateHandler.class);
        when(updateHandlerWithNonNullUpdateType.getUpdateType()).thenReturn(UpdateType.MESSAGE);
        mockedUpdateHandlers.add(updateHandlerWithNonNullUpdateType);
        mockedUpdateHandlers.add(updateHandlerWithNullUpdateType);

        Supplier<TelegramUpdateEventListener> constructorSupplier = () -> new TelegramUpdateEventListener(
                redisUserStateService, mockedUpdateHandlers, new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        assertThrows(HandlerTypeNotFoundException.class, constructorSupplier::get);
    }

    @Test
    @DisplayName("new TelegramUpdateEventListener - один из userState == null - проброшен HandlerTypeNotFoundException")
    void shouldThrowTelegramCommonExceptionIfUserStateOfStateHandlerIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);

        List<StateHandler> mockedStateHandlers = new ArrayList<>();
        StateHandler updateHandlerWithNullUpdateType = Mockito.mock(StateHandler.class);
        when(updateHandlerWithNullUpdateType.getUserState()).thenReturn(null);
        StateHandler updateHandlerWithNonNullUpdateType = Mockito.mock(StateHandler.class);
        when(updateHandlerWithNonNullUpdateType.getUserState()).thenReturn(() -> "someState");
        mockedStateHandlers.add(updateHandlerWithNonNullUpdateType);
        mockedStateHandlers.add(updateHandlerWithNullUpdateType);

        Supplier<TelegramUpdateEventListener> constructorSupplier = () -> new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), mockedStateHandlers,
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        assertThrows(HandlerTypeNotFoundException.class, constructorSupplier::get);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - апдейт пользователя в бане - прекращение выполнения метода")
    void shouldSkipIfUserIsBanned() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(true);
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(antiSpam, times(0)).isSpam(any());
        verify(redisUserStateService, times(0)).get(any());
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - апдейт является спамом - прекращение выполнения метода")
    void shouldSkipIfSpam() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(true);
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(redisUserStateService, times(0)).get(any());
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - апдейт для фильтра - вызов handle у фильтра")
    void shouldHandleFilter() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();
        List<UpdateFilter> updateFilters = new ArrayList<>();
        UpdateFilter mockedUpdateFilter = Mockito.mock(UpdateFilter.class);
        UpdateFilter mockedNonMatchUpdateFilter = Mockito.mock(UpdateFilter.class);
        when(mockedNonMatchUpdateFilter.match(update)).thenReturn(false);
        when(mockedUpdateFilter.match(update)).thenReturn(true);
        updateFilters.add(mockedNonMatchUpdateFilter);
        updateFilters.add(mockedUpdateFilter);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, updateFilters, antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(redisUserStateService, times(0)).get(any());
        verify(mockedUpdateFilter).handle(update);
    }


    @Test
    @DisplayName("update(TelegramUpdateEvent event) - сохранено состояние пользователя - вызов handle у обработчика состояния")
    void shouldHandleByStateHandler() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();
        List<StateHandler> stateHandlers = new ArrayList<>();
        StateHandler mockedStateHandler = Mockito.mock(StateHandler.class);
        UserState userState = () -> "someState";
        when(mockedStateHandler.getUserState()).thenReturn(userState);
        stateHandlers.add(mockedStateHandler);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), stateHandlers,
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);

        when(redisUserStateService.get(chatId)).thenReturn(userState);
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(mockedStateHandler).handle(update);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - не сохранено состояние - вызван empty обработчик")
    void shouldHandleByEmptyHandlerIfNoUserState() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);

        when(redisUserStateService.get(chatId)).thenReturn(null);
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(emptyHandler).getEmptyMessage(chatId);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - отсутствует обработчик для состояния - вызван empty обработчик")
    void shouldHandleByEmptyHandlerIfNoStateHandler() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);

        UserState userState = () -> "SOME_STATE";
        when(redisUserStateService.get(chatId)).thenReturn(userState);
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(emptyHandler).getEmptyMessage(chatId);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - не обрабатываемый обработчиком состояния тип апдейта - вызван empty обработчик")
    void shouldHandleByEmptyHandlerIfNotStateUpdateType() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);
        BotApiMethodMessage botApiMethodMessage = new SendMessage();
        when(emptyHandler.getEmptyMessage(chatId)).thenReturn(botApiMethodMessage);

        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        update.setChannelPost(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(emptyHandler).getEmptyMessage(chatId);
        verify(responseSender).execute(botApiMethodMessage);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - апдейт для обработчика апдейта - обработка передана на обработчик")
    void shouldHandleByUpdateHandler() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();
        List<UpdateHandler> updateHandlers = new ArrayList<>();
        UpdateHandler mockedUpdateHandler = Mockito.mock(UpdateHandler.class);
        when(mockedUpdateHandler.getUpdateType()).thenReturn(UpdateType.MESSAGE);
        when(mockedUpdateHandler.handle(update)).thenReturn(true);
        updateHandlers.add(mockedUpdateHandler);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, updateHandlers, new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);

        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(mockedUpdateHandler).handle(update);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - проброс исключения - отправка сообщения пользователю об ошибке")
    void shouldSendMessageIfExceptionThrown() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );

        Update update = new Update();
        Long chatId = 123456789L;
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        when(bannedCache.get(chatId)).thenThrow(NullPointerException.class);
        listener.update(event);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(responseSender).sendMessage(eq(chatId), messageCaptor.capture());
        String value = messageCaptor.getValue();
        assertAll(
                () -> assertTrue(value.startsWith("Произошла ошибка.")),
                () -> assertTrue(value.endsWith("Введите /start для выхода в главное меню."))
        );
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - не обрабатываемый обработчиком состояния тип апдейта - вызван empty обработчик, message не отправлен")
    void shouldHandleByEmptyHandlerWithoutSend() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        Update update = new Update();

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        Long chatId = 123456789L;
        when(bannedCache.get(chatId)).thenReturn(false);
        when(antiSpam.isSpam(chatId)).thenReturn(false);
        when(emptyHandler.getEmptyMessage(chatId)).thenReturn(null);

        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        update.setChannelPost(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);
        listener.update(event);
        verify(emptyHandler).getEmptyMessage(chatId);
        verify(responseSender, times(0)).execute(any());
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - отсутствует chat в апдейте - без ответа")
    void shouldSkipIfNoChatInUpdate() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        long chatId = 123456789L;
        Update update = new Update();
        PreCheckoutQuery preCheckoutQuery = new PreCheckoutQuery();
        User user = new User();
        user.setId(chatId);
        preCheckoutQuery.setFrom(user);
        update.setPreCheckoutQuery(preCheckoutQuery);
        listener.update(new TelegramUpdateEvent(new Object(), update));
        verify(emptyHandler, times(0)).getEmptyMessage(chatId);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - chat не является private - без ответа")
    void shouldSkipIfNotPrivateChatInUpdate() {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        long chatId = 123456789L;
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("group");
        message.setChat(chat);
        update.setChannelPost(message);
        listener.update(new TelegramUpdateEvent(new Object(), update));
        verify(emptyHandler, times(0)).getEmptyMessage(chatId);
    }

    @Test
    @DisplayName("update(TelegramUpdateEvent event) - поступление апдейтов во время обработки другого апдейта - должен пропустить обработку новых апдейтов")
    void shouldSkipUpdatesIfOneAlreadyProcessing() throws InterruptedException {
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);

        TelegramUpdateEventListener listener = new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        );
        long chatId = 123456789L;
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        update.setMessage(message);
        TelegramUpdateEvent event = new TelegramUpdateEvent(new Object(), update);

        try (ExecutorService executor = Executors.newFixedThreadPool(5);
             ExecutorService lockThreadExecutorService = Executors.newFixedThreadPool(1)) {
            CountDownLatch latch = new CountDownLatch(1);
            CountDownLatch latch2 = new CountDownLatch(1);

            when(bannedCache.get(chatId)).thenAnswer(invocationOnMock -> {
                latch.countDown();
                latch2.await();
                return true;
            });
            lockThreadExecutorService.execute(() -> listener.update(event));
            latch.await();
            for (int i = 0; i < 5; i++) {
                Update skipUpdate = new Update();
                Message skipUpdateMessage = new Message();
                Chat skipUpdateChat = new Chat();
                skipUpdateChat.setId(chatId);
                skipUpdateChat.setType("private");
                skipUpdateMessage.setChat(skipUpdateChat);
                skipUpdate.setMessage(skipUpdateMessage);
                executor.submit(() -> {
                    listener.update(new TelegramUpdateEvent(new Object(), skipUpdate));
                });
            }
            executor.shutdown();
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("Lock thread timed out");
            }
            latch2.countDown();
            lockThreadExecutorService.shutdown();
            if (!lockThreadExecutorService.awaitTermination(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("Lock thread timed out");
            }
            verify(bannedCache).get(chatId);
            listener.update(event);
            verify(bannedCache, times(2)).get(chatId);
        }
    }


}