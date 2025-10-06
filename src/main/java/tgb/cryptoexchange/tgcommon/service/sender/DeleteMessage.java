package tgb.cryptoexchange.tgcommon.service.sender;

import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public class DeleteMessage {

    public DeleteMessage(MethodExecutor methodExecutor, Long chatId, Integer messageId) {
        methodExecutor.execute(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build()
        );
    }
}
