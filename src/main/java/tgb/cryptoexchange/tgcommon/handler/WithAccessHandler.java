package tgb.cryptoexchange.tgcommon.handler;

/**
 * Обработчики с правами доступа
 */
public interface WithAccessHandler {

    /**
     * Проверка наличия доступа к обработчику
     * @param chatId чат айди пользователя
     * @return true если доступ есть, false если нет
     */
    boolean hasAccess(Long chatId);
}
