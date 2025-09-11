package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;

public class AnswerToInlineQuery {

    private final MethodExecutor methodExecutor;

    private final Integer inlineQueryId;

    private String title;

    private String messageText;

    private ParseMode parseMode;

    private String description;

    public AnswerToInlineQuery(MethodExecutor methodExecutor, Integer inlineQueryId) {
        this.methodExecutor = methodExecutor;
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
        methodExecutor.send(AnswerInlineQuery.builder()
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
