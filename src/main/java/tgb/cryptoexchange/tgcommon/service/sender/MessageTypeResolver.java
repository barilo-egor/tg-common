package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import java.util.List;

public class MessageTypeResolver {

    private final MethodExecutor methodExecutor;

    private final Long chatId;

    public MessageTypeResolver(MethodExecutor methodExecutor, Long chatId) {
        this.methodExecutor = methodExecutor;
        this.chatId = chatId;
    }

    public TextMessage message(String text) {
        return new TextMessage(methodExecutor, chatId, text);
    }

    public PhotoMessage photo(InputFile photo) {
        return new PhotoMessage(methodExecutor, chatId, photo);
    }

    public AnimationMessage animation(InputFile animation) {
        return new AnimationMessage(methodExecutor, chatId, animation);
    }

    public DocumentMessage document(InputFile document) {
        return new DocumentMessage(methodExecutor, chatId, document);
    }

    public MediaGroupMessage mediaGroup(List<InputMedia> inputMedia) {
        return new MediaGroupMessage(methodExecutor, chatId, inputMedia);
    }

    public EditTextMessage editText(String text) {
        return new EditTextMessage(methodExecutor, chatId, text);
    }

    public EditMessageReplyKeyboard editKeyboard(Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        return new EditMessageReplyKeyboard(methodExecutor, chatId, messageId, replyKeyboard);
    }
}
