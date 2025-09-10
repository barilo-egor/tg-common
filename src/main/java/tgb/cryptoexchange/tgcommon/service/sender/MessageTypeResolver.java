package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.objects.InputFile;

public class MessageTypeResolver {

    private final ResponseSender responseSender;

    private final Long chatId;

    public MessageTypeResolver(ResponseSender responseSender, Long chatId) {
        this.responseSender = responseSender;
        this.chatId = chatId;
    }

    public TextMessage message(String text) {
        return new TextMessage(responseSender, chatId, text);
    }

    public PhotoMessage photo(InputFile photo) {
        return new PhotoMessage(responseSender, chatId, photo);
    }

    public AnimationMessage animation(InputFile animation) {
        return new AnimationMessage(responseSender, chatId, animation);
    }

    public DocumentMessage document(InputFile document) {
        return new DocumentMessage(responseSender, chatId, document);
    }
}
