package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

public class AnimationMessage extends InputFileMessage<AnimationMessage> {

    public AnimationMessage(MethodExecutor methodExecutor, Long chatId, InputFile photo) {
        super(methodExecutor, chatId, photo);
    }

    public AnimationMessage caption(String caption) {
        this.caption = caption;
        return this;
    }

    public AnimationMessage replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

    public AnimationMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    @Override
    public AnimationMessage parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    @Override
    public Optional<Message> send() {
        return methodExecutor.execute(
                SendAnimation.builder()
                        .chatId(chatId)
                        .animation(inputFile)
                        .caption(caption)
                        .replyMarkup(replyKeyboard)
                        .replyToMessageId(replyToMessageId)
                        .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                        .build()
        );
    }
}
