package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Фильтр апдейтов
 */
public interface UpdateFilter {

    /**
     * Метод обработки апдейта
     * @param update апдейт пользователя
     */
    void handle(Update update);

    /**
     * Метод для определения является ли фильтр подходящим под апдейт
     * @param update апдейт пользователя
     * @return true, если фильтр подходит, false если нет
     */
    boolean match(Update update);
}
