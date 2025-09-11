package tgb.cryptoexchange.tgcommon.service.sender;

import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public class DeleteMessage {

    private final MethodExecutor methodExecutor;

    private final Long chatId;

    private final Integer messageId;

    public DeleteMessage(MethodExecutor methodExecutor, Long chatId, Integer messageId) {
        this.methodExecutor = methodExecutor;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public void delete() {
        methodExecutor.execute(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build()
        );
    }
}
