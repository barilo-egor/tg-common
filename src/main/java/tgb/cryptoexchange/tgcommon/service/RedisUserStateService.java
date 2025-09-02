package tgb.cryptoexchange.tgcommon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.tgcommon.constants.UserState;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Сервис для работы с состояние пользователя. Если от пользователя требуется ввод или отправка чего либо, для него следует
 * сохранить состояние, для которого есть свой обработчик. Если за пользователем будет сохранено состояние, то
 * независимо от типа апдейта обработка будет осуществлена обработчиком данного состояния.
 */
@Service
public class RedisUserStateService {

    private final String prefix;

    private final RedisTemplate<String, UserState> redisTemplate;

    public RedisUserStateService(RedisTemplate<String, UserState> redisTemplate, @Value("${bot.username}") String botName) {
        this.redisTemplate = redisTemplate;
        this.prefix = botName + ":" + "state_";
    }

    /**
     * Сохранение состояния пользователя
     *
     * @param chatId чат айди пользователя
     * @param state  состояние пользователя
     */
    public void save(Long chatId, UserState state) {
        redisTemplate.opsForValue().set(prefix + chatId, state, Duration.of(20, ChronoUnit.MINUTES));
    }

    /**
     * Сохранение состояния пользователя
     *
     * @param chatId          чат айди пользователя
     * @param state           состояние пользователя
     * @param durationMinutes продолжительность хранения в минутах
     */
    public void save(Long chatId, UserState state, int durationMinutes) {
        redisTemplate.opsForValue().set(prefix + chatId, state, Duration.of(durationMinutes, ChronoUnit.MINUTES));
    }

    /**
     * Получение состояния польхователя по его чат айди
     * @param chatId чат айди пользователя
     * @return состояние пользователя, либо null
     */
    public UserState get(Long chatId) {
        return redisTemplate.opsForValue().get(prefix + chatId);
    }

    /**
     * Удаление состояния пользователя
     * @param chatId чат айди пользователя
     */
    public void delete(Long chatId) {
        redisTemplate.delete(prefix + chatId);
    }
}
