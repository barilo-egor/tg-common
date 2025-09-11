package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnswerToInlineQueryTest {

    @ParameterizedTest
    @CsvSource({
            "1,t,m,HTML,d",
            "1235135,some title,some message,NONE,some description",
            "56547676,TiTlE,message message message message message message,,description"
    })
    void shouldCreateWithPassedParameters(Integer inlineQueryId, String title, String messageText,
                                          ParseMode parseMode, String description) {
        MethodExecutor methodExecutor = Mockito.mock(MethodExecutor.class);
        new AnswerToInlineQuery(methodExecutor, inlineQueryId)
                .title(title)
                .messageText(messageText)
                .parseMode(parseMode)
                .description(description)
                .send();
        ArgumentCaptor<AnswerInlineQuery> answerInlineQueryArgumentCaptor = ArgumentCaptor.forClass(AnswerInlineQuery.class);
        verify(methodExecutor).execute(answerInlineQueryArgumentCaptor.capture());
        AnswerInlineQuery actualInlineQuery = answerInlineQueryArgumentCaptor.getValue();
        InlineQueryResultArticle inlineQueryResultArticle = (InlineQueryResultArticle) actualInlineQuery.getResults().getFirst();
        InputTextMessageContent inputTextMessageContent = (InputTextMessageContent) inlineQueryResultArticle.getInputMessageContent();
        assertAll(
                () -> assertEquals(inlineQueryId.toString(), actualInlineQuery.getInlineQueryId()),
                () -> assertEquals(title, inlineQueryResultArticle.getTitle()),
                () -> assertEquals(messageText, inputTextMessageContent.getMessageText()),
                () -> assertEquals(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue(), inputTextMessageContent.getParseMode()),
                () -> assertEquals(description, inlineQueryResultArticle.getDescription())
        );
    }
}