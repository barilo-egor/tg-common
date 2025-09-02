package tgb.cryptoexchange.tgcommon.handler;

/**
 * Интерфейс для антиспама
 */
public interface AntiSpam {

    /**
     * Метод для определния является ли апдейт спамом
     * @param chatId чат айди пользователя автора апдейта
     * @return true, если апдейт является спамом и false, если нет
     */
    boolean isSpam(Long chatId);
}
