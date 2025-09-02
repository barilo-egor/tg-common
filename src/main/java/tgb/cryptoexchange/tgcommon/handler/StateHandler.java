package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UserState;

/**
 * Обработчик состояний пользователя
 */
public interface StateHandler {

    /**
     * Метод обработки апдейта
     * @param update апдейт пользователя
     */
    void handle(Update update);

    /**
     * Состояние, которое обрабатывает данный обработчик. Используется для определения обработчика для апдейта.
     * @return обрабатываемое состояние
     */
    UserState getUserState();
}
