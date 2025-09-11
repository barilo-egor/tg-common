package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public class EditMessageReplyKeyboard {

    private final MethodExecutor methodExecutor;

    private final Long chatId;

    private final Integer messageId;

    private final InlineKeyboardMarkup replyKeyboard;

    public EditMessageReplyKeyboard(MethodExecutor methodExecutor, Long chatId, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        this.methodExecutor = methodExecutor;
        this.chatId = chatId;
        this.messageId = messageId;
        this.replyKeyboard = replyKeyboard;
    }

    public void send() {
        methodExecutor.execute(EditMessageReplyMarkup.builder().chatId(chatId).messageId(messageId).replyMarkup(replyKeyboard).build());
    }
}
