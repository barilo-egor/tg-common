package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;

/**
 * Обработчик типа апдейта
 */
public interface UpdateHandler {

    /**
     * Обработка апдейта
     * @param update апдейт, который следует обработать
     */
    boolean handle(Update update);

    /**
     * Тип обрабатываемых апдейтов
     * @return тип апдейта
     */
    UpdateType getUpdateType();
}
