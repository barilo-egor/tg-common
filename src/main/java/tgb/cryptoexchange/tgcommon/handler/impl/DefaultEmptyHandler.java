package tgb.cryptoexchange.tgcommon.handler.impl;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tgb.cryptoexchange.tgcommon.handler.EmptyHandler;

public class DefaultEmptyHandler implements EmptyHandler {

    @Override
    public BotApiMethodMessage getEmptyMessage(Long chatId) {
        return SendMessage.builder().chatId(chatId).text("Что-то пошло не так.").build();
    }
}
