package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;

public class EditTextMessage {

    protected final ResponseSender responseSender;

    protected final Long chatId;

    protected final String text;

    protected InlineKeyboardMarkup replyKeyboard;

    protected Integer replyToMessageId;

    protected ParseMode parseMode;

    public EditTextMessage(ResponseSender responseSender, Long chatId, String text) {
        this.responseSender = responseSender;
        this.chatId = chatId;
        this.text = text;
    }

    public EditTextMessage replyKeyboard(InlineKeyboardMarkup replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    public EditTextMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    public EditTextMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public void send() {
        responseSender.send(
                EditMessageText.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(replyKeyboard)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
