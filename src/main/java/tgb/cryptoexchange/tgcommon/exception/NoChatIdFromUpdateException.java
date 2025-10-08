package tgb.cryptoexchange.tgcommon.exception;

/**
 * Пробрасывается для того, чтобы показать, что чат айди в апдейте найден не был.
 */
public class NoChatIdFromUpdateException extends TelegramCommonException {
    public NoChatIdFromUpdateException(String message) {
        super(message);
    }
}
