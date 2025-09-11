package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResponseSenderTest {

    @Mock
    private MethodExecutor methodExecutor;

    @InjectMocks
    private ResponseSender responseSender;

    @ParameterizedTest
    @ValueSource(longs = {123456789L, 987654321L, 124543745L})
    @DisplayName("to(Long chatId) - передан chat id - должен выполнить отправку message по переданному chat id")
    void toShouldCreateMessageResolverWithPassedChatId(Long chatId) {
        responseSender.to(chatId)
                .message("text")
                .send();
        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(methodExecutor).execute(sendMessageArgumentCaptor.capture());
        SendMessage actual = sendMessageArgumentCaptor.getValue();
        assertEquals(chatId.toString(), actual.getChatId());
    }

    @ParameterizedTest
    @ValueSource(ints = {123456, 1, 6543})
    void answerToInlineQueryShouldCreateWithPassedInlineQueryId(Integer inlineQueryId) {
        responseSender.answerToInlineQuery(inlineQueryId, "title", "messageText")
                .send();
        ArgumentCaptor<AnswerInlineQuery> answerInlineQueryArgumentCaptor = ArgumentCaptor.forClass(AnswerInlineQuery.class);
        verify(methodExecutor).execute(answerInlineQueryArgumentCaptor.capture());
        assertEquals(inlineQueryId.toString(), answerInlineQueryArgumentCaptor.getValue().getInlineQueryId());
    }
}