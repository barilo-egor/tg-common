package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

public class DocumentMessage extends InputFileMessage<DocumentMessage> {

    public DocumentMessage(MethodExecutor methodExecutor, Long chatId, InputFile photo) {
        super(methodExecutor, chatId, photo);
    }

    public DocumentMessage caption(String caption) {
        this.caption = caption;
        return this;
    }

    public DocumentMessage replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    public DocumentMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    @Override
    public DocumentMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    @Override
    public Optional<Message> send() {
        return methodExecutor.execute(
                SendDocument.builder()
                        .chatId(chatId)
                        .document(inputFile)
                        .caption(caption)
                        .replyMarkup(replyKeyboard)
                        .replyToMessageId(replyToMessageId)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
