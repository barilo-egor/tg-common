package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

public class VideoMessage extends InputFileMessage<VideoMessage> {

    public VideoMessage(MethodExecutor methodExecutor, Long chatId, InputFile inputFile) {
        super(methodExecutor, chatId, inputFile);
    }

    @Override
    public VideoMessage replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    @Override
    public VideoMessage caption(String caption) {
        this.caption = caption;
        return this;
    }

    @Override
    public VideoMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    @Override
    public VideoMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    @Override
    public Optional<Message> send() {
        return methodExecutor.execute(
                SendVideo.builder()
                        .chatId(chatId)
                        .video(inputFile)
                        .caption(caption)
                        .replyMarkup(replyKeyboard)
                        .replyToMessageId(replyToMessageId)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
