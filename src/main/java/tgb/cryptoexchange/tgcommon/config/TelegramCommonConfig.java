package tgb.cryptoexchange.tgcommon.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tgb.cryptoexchange.tgcommon.handler.EmptyHandler;
import tgb.cryptoexchange.tgcommon.handler.impl.DefaultEmptyHandler;

@Configuration
@ComponentScan(basePackages = {"org.telegram"})
public class TelegramCommonConfig {

    @ConditionalOnMissingBean(EmptyHandler.class)
    @Bean
    public EmptyHandler emptyHandler() {
        return new DefaultEmptyHandler();
    }
}
