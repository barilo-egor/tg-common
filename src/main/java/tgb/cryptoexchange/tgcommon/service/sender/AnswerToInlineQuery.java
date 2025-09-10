package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;

public class AnswerToInlineQuery {

    private final ResponseSender responseSender;

    private final Integer inlineQueryId;

    private String title;

    private String messageText;

    private ParseMode parseMode;

    private String description;

    public AnswerToInlineQuery(ResponseSender responseSender, Integer inlineQueryId) {
        this.responseSender = responseSender;
        this.inlineQueryId = inlineQueryId;
    }

    public AnswerToInlineQuery title(String title) {
        this.title = title;
        return this;
    }

    public AnswerToInlineQuery messageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public AnswerToInlineQuery parseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public AnswerToInlineQuery description(String description) {
        this.description = description;
        return this;
    }

    public void send() {
        responseSender.send(AnswerInlineQuery.builder()
                .inlineQueryId(inlineQueryId.toString())
                .result(InlineQueryResultArticle.builder()
                        .id(inlineQueryId.toString())
                        .title(title)
                        .inputMessageContent(InputTextMessageContent.builder()
                                .messageText(messageText)
                                .parseMode(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue())
                                .build())
                        .description(description)
                        .build()
                ).build());
    }
}
