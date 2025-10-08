package tgb.cryptoexchange.tgcommon.exception;

/**
 * Пробрасывается для того, чтобы показать, что обработчик апдейта не был найден.
 */
public class HandlerTypeNotFoundException extends TelegramCommonException {

    public HandlerTypeNotFoundException(String message) {
        super(message);
    }
}
