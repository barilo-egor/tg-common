package tgb.cryptoexchange.tgcommon.handler;

public interface BannedCache {

    /**
     * Метод для определния находится ли пользователь в бане.
     * @param chatId чат айди пользователя автора апдейта
     * @return true, если пользователь в бане и false, если нет
     */
    boolean get(Long chatId);
}
