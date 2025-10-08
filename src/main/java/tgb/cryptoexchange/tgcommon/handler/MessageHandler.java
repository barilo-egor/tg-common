package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.MessageType;

/**
 * Интерфейс для обработчиков апдейтов типа {@link tgb.cryptoexchange.tgcommon.constants.UpdateType#MESSAGE}
 */
public interface MessageHandler {

    /**
     * Метод обработки апдейта с message
     * @param message месседж из апдейта
     * @return был ли обработан апдейт
     */
    boolean handleMessage(Message message);

    /**
     * Тип месседжа для определения обработчика
     * @return тип месседжа
     */
    MessageType getMessageType();
}
