package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс для обработчиков месседжей с текстом типа {@link tgb.cryptoexchange.tgcommon.constants.TextMessageType#TEXT_COMMAND}
 */
public interface TextCommandHandler extends WithAccessHandler {

    /**
     * Обработка месседжа
     * @param message месседж из апдейта
     */
    void handle(Message message);

    /**
     * Текст команды для срабатывания обработчика
     * @return команда в виде текста
     */
    String getTextCommand();
}
