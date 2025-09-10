package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

public class TextMessage {

    protected final ResponseSender responseSender;

    protected final Long chatId;

    protected final String text;

    protected ReplyKeyboard replyKeyboard;

    protected Integer replyToMessageId;

    protected ParseMode parseMode;

    public TextMessage(ResponseSender responseSender, Long chatId, String text) {
        this.responseSender = responseSender;
        this.chatId = chatId;
        this.text = text;
    }

    public TextMessage replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    public TextMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    public TextMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public Optional<Message> send() {
        return responseSender.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(replyKeyboard)
                        .replyToMessageId(replyToMessageId)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
