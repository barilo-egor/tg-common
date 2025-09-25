package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimationMessageTest {

    @ParameterizedTest
    @CsvSource({
            "541551235,AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ,caption1,4535345,HTML",
            "12345567987,AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ,c,123,MARKDOWN",
            "8975325955953,AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE,longlonglonglonglonglonglonglonglonglonglonglonglonglongcaption,15252352,",
    })
    void shouldCreateAnimationMessageWithPassedParameter(Long chatId, String fileId, String caption, Integer replyToMessageId, ParseMode parseMode) {
        MethodExecutor methodExecutor = Mockito.mock(MethodExecutor.class);
        Message expectedMessage = new Message();
        when(methodExecutor.execute(any(SendAnimation.class))).thenReturn(Optional.of(expectedMessage));
        ReplyKeyboard expectedKeyboard = new InlineKeyboardMarkup();
        new AnimationMessage(methodExecutor, chatId, new InputFile(fileId))
                .caption(caption)
                .replyToMessageId(replyToMessageId)
                .parseMode(parseMode)
                .replyKeyboard(expectedKeyboard)
                .send();
        ArgumentCaptor<SendAnimation> sendAnimationArgumentCaptor = ArgumentCaptor.forClass(SendAnimation.class);
        verify(methodExecutor).execute(sendAnimationArgumentCaptor.capture());
        SendAnimation actual = sendAnimationArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(chatId.toString(), actual.getChatId()),
                () -> assertEquals(fileId, actual.getAnimation().getAttachName()),
                () -> assertEquals(caption, actual.getCaption()),
                () -> assertEquals(replyToMessageId, actual.getReplyToMessageId()),
                () -> assertEquals(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue() , actual.getParseMode())
        );
    }
}