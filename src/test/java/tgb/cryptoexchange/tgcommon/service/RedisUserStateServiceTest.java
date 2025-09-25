package tgb.cryptoexchange.tgcommon.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import tgb.cryptoexchange.tgcommon.constants.UserState;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisUserStateServiceTest {

    @ValueSource(strings = {"devbot", "exchangeCryptoBot", "123bot"})
    @ParameterizedTest
    void shouldCreateRedisUserStateServiceWithPrefixWithPassedBotName(String botName) {
        RedisTemplate<String, UserState> redisTemplate = Mockito.mock(new TypeReference<RedisTemplate<String, UserState>>() {}.getType().getTypeName());
        RedisUserStateService redisUserStateService = new RedisUserStateService(redisTemplate, botName);
        ValueOperations<String, UserState> valueOperations = Mockito.mock(new TypeReference<ValueOperations<String, UserState>>() {}.getType().getTypeName());
        Long chatId = 12345678L;
        UserState userState = () -> "state";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisUserStateService.save(chatId, userState);
        verify(valueOperations).set(eq(botName + ":state_" + chatId), any(), any());
    }

    @ParameterizedTest
    @CsvSource({
            "123456789,state",
            "987654321,q",
            "12345678,someLongStateWithNumbers12354"
    })
    void saveShouldSetWithPassedParameters(Long chatId, String state) {
        UserState userState = () -> state;
        RedisTemplate<String, UserState> redisTemplate = Mockito.mock(new TypeReference<RedisTemplate<String, UserState>>() {}.getType().getTypeName());
        RedisUserStateService redisUserStateService = new RedisUserStateService(redisTemplate, "botName");
        ValueOperations<String, UserState> valueOperations = Mockito.mock(new TypeReference<ValueOperations<String, UserState>>() {}.getType().getTypeName());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisUserStateService.save(chatId, userState);
        ArgumentCaptor<Duration> durationArgumentCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOperations).set(eq("botName:state_" + chatId), eq(userState), durationArgumentCaptor.capture());
        Duration duration = durationArgumentCaptor.getValue();
        assertEquals(1200, duration.get(ChronoUnit.SECONDS));
    }

    @ParameterizedTest
    @CsvSource({
            "123456789,state,200",
            "987654321,q,2",
            "12345678,someLongStateWithNumbers12354,1"
    })
    void saveShouldSetWithPassedParametersWithDurationMinutes(Long chatId, String state, int value) {
        UserState userState = () -> state;
        RedisTemplate<String, UserState> redisTemplate = Mockito.mock(new TypeReference<RedisTemplate<String, UserState>>() {}.getType().getTypeName());
        RedisUserStateService redisUserStateService = new RedisUserStateService(redisTemplate, "botName");
        ValueOperations<String, UserState> valueOperations = Mockito.mock(new TypeReference<ValueOperations<String, UserState>>() {}.getType().getTypeName());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisUserStateService.save(chatId, userState, value);
        ArgumentCaptor<Duration> durationArgumentCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOperations).set(eq("botName:state_" + chatId), eq(userState), durationArgumentCaptor.capture());
        Duration duration = durationArgumentCaptor.getValue();
        assertEquals(value * 60L, duration.get(ChronoUnit.SECONDS));
    }

    @ParameterizedTest
    @ValueSource(longs = {123456789L, 987654321L, 12344321L})
    void getShouldReturnValueByPrefixAndChatId(Long chatId) {
        UserState userState = () -> "state";
        RedisTemplate<String, UserState> redisTemplate = Mockito.mock(new TypeReference<RedisTemplate<String, UserState>>() {}.getType().getTypeName());
        RedisUserStateService redisUserStateService = new RedisUserStateService(redisTemplate, "botName");
        ValueOperations<String, UserState> valueOperations = Mockito.mock(new TypeReference<ValueOperations<String, UserState>>() {}.getType().getTypeName());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("botName:state_" + chatId)).thenReturn(userState);
        assertEquals(userState, redisUserStateService.get(chatId));
    }

    @ParameterizedTest
    @ValueSource(longs = {123456789L, 987654321L, 12344321L})
    void deleteShouldDeleteByPrefixAndChatId(Long chatId) {
        RedisTemplate<String, UserState> redisTemplate = Mockito.mock(new TypeReference<RedisTemplate<String, UserState>>() {}.getType().getTypeName());
        RedisUserStateService redisUserStateService = new RedisUserStateService(redisTemplate, "botName");
        redisUserStateService.delete(chatId);
        verify(redisTemplate).delete("botName:state_" + chatId);
    }
}