package tgb.cryptoexchange.tgcommon.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Инстанс бота, реализация TelegramLongPollingBot.<br>
 * Требует пропертей bot.token и bot.username которые можно получить у <a href="https://t.me/BotFather">BotFather</a>
 */
@Component
@Slf4j
public class BotInstance extends TelegramLongPollingBot {

    private final String username;

    private final ApplicationEventPublisher eventPublisher;

    public BotInstance(@Value("${bot.token}") String botToken,
                       @Value("${bot.username}") String username,
                       ApplicationEventPublisher eventPublisher) {
        super(botToken);
        this.username = username;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void onUpdateReceived(Update update) {
        log.trace("Получен telegram update: {}", update);
        eventPublisher.publishEvent(new TelegramUpdateEvent(this, update));
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }
}
