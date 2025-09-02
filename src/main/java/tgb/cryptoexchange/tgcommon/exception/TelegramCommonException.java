package tgb.cryptoexchange.tgcommon.exception;

/**
 * Общее исключение библиотеки
 */
public class TelegramCommonException extends RuntimeException {
    public TelegramCommonException(String message) {
        super(message);
    }

  public TelegramCommonException(String message, Throwable cause) {
    super(message, cause);
  }
}
