package tgb.cryptoexchange.tgcommon.handler.impl;

import tgb.cryptoexchange.tgcommon.handler.EmptyHandler;
import tgb.cryptoexchange.tgcommon.service.sender.ResponseSender;

public class DefaultEmptyHandler implements EmptyHandler {

    private final ResponseSender responseSender;

    public DefaultEmptyHandler(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @Override
    public void handle(Long chatId) {
        responseSender.to(chatId)
                .message("Что-то пошло не так.")
                .send();
    }
}
