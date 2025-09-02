package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;

/**
 * Интерфейс для обработчиков апдейтов типа {@link tgb.cryptoexchange.tgcommon.constants.UpdateType#MESSAGE} с текстом
 */
public interface TextHandler {

    /**
     * Метод обработки апдейта с message
     * @param message месседж из апдейта
     * @return был ли обработан апдейт
     */
    boolean handle(Message message);

    /**
     * Тип текста для определения обработчика
     * @return тип текста
     */
    TextMessageType getTextMessageType();
}
