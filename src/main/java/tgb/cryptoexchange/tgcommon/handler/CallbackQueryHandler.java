package tgb.cryptoexchange.tgcommon.handler;

import tgb.cryptoexchange.tgcommon.keyboard.PressedInlineButton;

/**
 * Интерфейс для обработчиков апдейтов типа {@link tgb.cryptoexchange.tgcommon.constants.UpdateType#CALLBACK_QUERY}
 */
public interface CallbackQueryHandler extends WithAccessHandler {

    /**
     * Метод обработки апдейта
     * @param button кнопка, которую нажал пользователь
     */
    void handle(PressedInlineButton button);

    /**
     * Используется для определения обработчика для апдейта.
     * @return уникальная строка для данного обработчика
     */
    String getId();
}
