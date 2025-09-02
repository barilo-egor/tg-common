package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;

/**
 * Обработчик, вызываемый в случае, если не был найден подходящий обработчик для апдейта
 */
public interface EmptyHandler {

    /**
     * Метод для получения сообщения, которое будет отправлено пользователю, в случае если подходящий обработчик не был найден
     * @param chatId чат айди пользователя
     * @return сообщение, отправляемое пользователю
     */
    BotApiMethodMessage getEmptyMessage(Long chatId);
}
