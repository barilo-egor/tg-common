package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

public class PhotoMessage extends InputFileMessage<PhotoMessage> {

    public PhotoMessage(ResponseSender responseSender, Long chatId, InputFile photo) {
        super(responseSender, chatId, photo);
    }

    public PhotoMessage caption(String caption) {
        this.caption = caption;
        return this;
    }

    @Override
    public PhotoMessage replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    public PhotoMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    @Override
    public PhotoMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    @Override
    public Optional<Message> send() {
        return responseSender.send(
                SendPhoto.builder()
                        .chatId(chatId)
                        .photo(inputFile)
                        .caption(caption)
                        .replyMarkup(replyKeyboard)
                        .replyToMessageId(replyToMessageId)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
