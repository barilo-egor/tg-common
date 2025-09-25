package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnswerToCallbackQueryTest {

    @ParameterizedTest
    @CsvSource({
            "some text,someId,true",
            "s,123523,false",
            "123321,qwasdasrqr,true"
    })
    void shouldCreateWithPassedParameters(String text, String callbackQueryId, boolean showAlert) {
        MethodExecutor methodExecutor = Mockito.mock(MethodExecutor.class);
        new AnswerToCallbackQuery(methodExecutor, text, callbackQueryId)
                .showAlert(showAlert)
                .send();
        var answerToCallbackQueryArgumentCaptor = ArgumentCaptor.forClass(AnswerCallbackQuery.class);
        verify(methodExecutor).execute(answerToCallbackQueryArgumentCaptor.capture());
        AnswerCallbackQuery actual = answerToCallbackQueryArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(text, actual.getText()),
                () -> assertEquals(callbackQueryId, actual.getCallbackQueryId()),
                () -> assertEquals(showAlert, actual.getShowAlert())
        );
    }
}