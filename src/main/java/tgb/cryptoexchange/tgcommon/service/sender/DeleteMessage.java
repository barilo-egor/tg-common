package tgb.cryptoexchange.tgcommon.service.sender;

public class DeleteMessage {

    private final ResponseSender responseSender;

    private final Long chatId;

    private Integer messageId;

    public DeleteMessage(ResponseSender responseSender, Long chatId) {
        this.responseSender = responseSender;
        this.chatId = chatId;
    }

    public DeleteMessage messageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public void delete() {
        responseSender.send(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build()
        );
    }
}
