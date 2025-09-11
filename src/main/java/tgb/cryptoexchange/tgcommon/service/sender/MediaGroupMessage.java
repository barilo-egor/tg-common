package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import java.util.List;

public class MediaGroupMessage {

    private final MethodExecutor methodExecutor;

    private final Long chatId;

    private final List<InputMedia> media;

    private Integer replyToMessageId;

    public MediaGroupMessage(MethodExecutor methodExecutor, Long chatId, List<InputMedia> media) {
        this.methodExecutor = methodExecutor;
        this.chatId = chatId;
        this.media = media;
    }

    public MediaGroupMessage replyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    public void send() {
        methodExecutor.send(
                SendMediaGroup.builder()
                        .chatId(chatId)
                        .medias(media)
                        .replyToMessageId(replyToMessageId)
                        .build()
        );
    }
}
