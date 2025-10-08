package tgb.cryptoexchange.tgcommon.handler;

/**
 * Обработчик, вызываемый в случае, если не был найден подходящий обработчик для апдейта
 */
public interface EmptyHandler {

    /**
     * @param chatId чат айди пользователя
     */
    void handle(Long chatId);
}
