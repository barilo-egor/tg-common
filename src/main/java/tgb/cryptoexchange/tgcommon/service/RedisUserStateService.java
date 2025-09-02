package tgb.cryptoexchange.tgcommon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.tgcommon.constants.UserState;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class RedisUserStateService {

    private final String prefix;

    private final RedisTemplate<String, UserState> redisTemplate;

    public RedisUserStateService(RedisTemplate<String, UserState> redisTemplate, @Value("${bot.username}") String botName) {
        this.redisTemplate = redisTemplate;
        this.prefix = botName + ":" + "state_";
    }

    public void save(Long chatId, UserState state) {
        redisTemplate.opsForValue().set(prefix + chatId, state, Duration.of(20, ChronoUnit.MINUTES));
    }

    public void save(Long chatId, UserState state, int durationMinutes) {
        redisTemplate.opsForValue().set(prefix + chatId, state, Duration.of(durationMinutes, ChronoUnit.MINUTES));
    }

    public UserState get(Long key) {
        return redisTemplate.opsForValue().get(prefix + key);
    }

    public void delete(Long key) {
        redisTemplate.delete(prefix + key);
    }
}
