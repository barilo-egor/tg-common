package tgb.cryptoexchange.tgcommon.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
@Slf4j
public class MethodExecutor {

    private final BotInstance bot;

    public MethodExecutor(BotInstance bot) {
        this.bot = bot;
    }

    public Optional<Message> send(SendMediaBotMethod<?> sendMediaBotMethod) {
        try {
            switch (sendMediaBotMethod) {
                case SendPhoto sendPhoto -> {
                    return Optional.of(bot.execute(sendPhoto));
                }
                case SendAnimation sendAnimation -> {
                    return Optional.of(bot.execute(sendAnimation));
                }
                case SendVideo sendVideo -> {
                    return Optional.of(bot.execute(sendVideo));
                }
                case SendDocument sendDocument -> {
                    return Optional.of(bot.execute(sendDocument));
                }
                default -> throw new UnsupportedOperationException("Unexpected value: " + sendMediaBotMethod.getClass());
            }
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке {} :", sendMediaBotMethod.toString(), e);
            return Optional.empty();
        }
    }

    public void send(BotApiMethodBoolean botApiMethodBoolean) {
        try {
            bot.execute(botApiMethodBoolean);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке {} :", botApiMethodBoolean.toString(), e);
        }
    }

    public void send(BotApiMethodSerializable botApiMethodSerializable) {
        try {
            bot.execute(botApiMethodSerializable);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке {} :", botApiMethodSerializable.toString(), e);
        }
    }

    public void send(SendMediaGroup sendMediaGroup) {
        try {
            bot.execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке {} :", sendMediaGroup.toString(), e);
        }
    }

    public Optional<Message> send(BotApiMethodMessage botApiMethodMessage) {
        try {
            return Optional.of(bot.execute(botApiMethodMessage));
        } catch (TelegramApiException e) {
            return Optional.empty();
        }
    }
}
