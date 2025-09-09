package tgb.cryptoexchange.tgcommon.serialize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.handler.StateHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserStateRedisSerializerTest {

    @ParameterizedTest
    @ValueSource(strings = {"state1", "SOME_STATE", "123"})
    @DisplayName("serialize(UserState value) - передан UserState на который есть обработчик " +
            "- возвращен сериализованный стейт в виде массива байт")
    void serializeShouldSerializeState(String state) {
        List<StateHandler> userStates = new ArrayList<>();
        UserState userState = () -> state;
        userStates.add(new StateHandler() {
            @Override
            public void handle(Update update) {
                // stub
            }

            @Override
            public UserState getUserState() {
                return userState;
            }
        });
        byte[] expected = state.getBytes();
        var userStateSerializer = new UserStateRedisSerializer(userStates);
        assertArrayEquals(expected, userStateSerializer.serialize(userState));
    }

    @Test
    @DisplayName("serialize(UserState value) - передан null - возвращен пустой массив байтов")
    void serializeShouldReturnEmptyByteArrayIfNullState() {
        var userStateSerializer = new UserStateRedisSerializer(new ArrayList<>());
        byte[] actual = userStateSerializer.serialize(null);
        assertNotNull(actual);
        assertEquals(0, actual.length);
    }

    @ParameterizedTest
    @ValueSource(strings = {"state1", "123", "q"})
    @DisplayName("deserialize(byte[] bytes) - передан массив байтов с идентификатором стейта - возвращен стейт")
    void deserializeShouldReturnUserState(String state) {
        List<StateHandler> userStates = new ArrayList<>();
        UserState expected = () -> state;
        userStates.add(new StateHandler() {

            @Override
            public void handle(Update update) {

            }

            @Override
            public UserState getUserState() {
                return expected;
            }
        });
        userStates.add(new StateHandler() {
            @Override
            public void handle(Update update) {

            }

            @Override
            public UserState getUserState() {
                return () -> "another state";
            }
        });

        var userStateSerializer = new UserStateRedisSerializer(userStates);
        UserState actual = userStateSerializer.deserialize(state.getBytes());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deserialize(byte[] bytes) - передан null - возвращен null")
    void shouldReturnNullIfPassedNullParameter() {
        var userStateSerializer = new UserStateRedisSerializer(new ArrayList<>());
        assertNull(userStateSerializer.deserialize(null));
    }

    @Test
    @DisplayName("deserialize(byte[] bytes) - передан пустой массив байтов - возвращен null")
    void shouldReturnNullIfPassedEmptyByteArray() {
        var userStateSerializer = new UserStateRedisSerializer(new ArrayList<>());
        assertNull(userStateSerializer.deserialize(new byte[0]));
    }
}