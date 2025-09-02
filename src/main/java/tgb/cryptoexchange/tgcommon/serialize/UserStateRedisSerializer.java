package tgb.cryptoexchange.tgcommon.serialize;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.handler.StateHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserStateRedisSerializer implements RedisSerializer<UserState> {

    private final Map<String, UserState> states;

    public UserStateRedisSerializer(List<StateHandler> stateHandlers) {
        this.states = new HashMap<>();
        for (StateHandler stateHandler : stateHandlers) {
            this.states.put(stateHandler.getUserState().getState(), stateHandler.getUserState());
        }
    }

    @Override
    public byte[] serialize(UserState value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }
        return value.getState().getBytes();
    }

    @Override
    public UserState deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String name = new String(bytes);
        return states.get(name);
    }
}
