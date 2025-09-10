package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Optional;

public abstract class InputFileMessage<T> {

    protected final ResponseSender responseSender;

    protected final Long chatId;

    protected final InputFile inputFile;

    protected ReplyKeyboard replyKeyboard;

    protected String caption;

    protected Integer replyToMessageId;

    protected ParseMode parseMode;

    public InputFileMessage(ResponseSender responseSender, Long chatId, InputFile inputFile) {
        this.responseSender = responseSender;
        this.chatId = chatId;
        this.inputFile = inputFile;
    }

    public abstract T replyKeyboard(ReplyKeyboard replyKeyboard);

    public abstract T caption(String caption);

    public abstract T replyToMessageId(Integer replyToMessageId);

    public abstract T parseMode(ParseMode parseMode);

    public abstract Optional<Message> send();

}
