package tgb.cryptoexchange.tgcommon.service.sender;

import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public class DeleteMessage {

    private final MethodExecutor methodExecutor;

    private final Long chatId;

    private Integer messageId;

    public DeleteMessage(MethodExecutor methodExecutor, Long chatId) {
        this.methodExecutor = methodExecutor;
        this.chatId = chatId;
    }

    public DeleteMessage messageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public void delete() {
        methodExecutor.send(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build()
        );
    }
}
