package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс для обработчиков месседжей с текстом типа {@link tgb.cryptoexchange.tgcommon.constants.TextMessageType#SLASH_COMMAND}
 */
public interface SlashCommandHandler {

    /**
     * Обработка месседжа
     * @param message месседж из апдейта
     */
    void handle(Message message);

    /**
     * Текст команды для срабатывания обработчика.
     * Можно указывать вместе со слешем вначале, либо без него.
     * @return команда в виде текста
     */
    String getSlashCommand();
}
