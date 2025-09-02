package tgb.cryptoexchange.tgcommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.handler.StateHandler;
import tgb.cryptoexchange.tgcommon.serialize.UserStateRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserState> redisUserStateTemplate(RedisConnectionFactory connectionFactory,
                                                                   List<StateHandler> stateHandlers) {
        RedisTemplate<String, UserState> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new UserStateRedisSerializer(stateHandlers));
        return template;
    }
}
