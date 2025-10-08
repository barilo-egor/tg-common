package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;
import tgb.cryptoexchange.tgcommon.constants.ParseMode;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EditTextMessageTest {

    @ParameterizedTest
    @CsvSource({
            "12354323,453345,new message text,MARKDOWN",
            "12354323,15352525,another long message another long message another long message another long message ,HTML",
            "123424323525,123,t,"
    })
    void shouldCreateWithPassedParameters(Long chatId, Integer messageId, String text, ParseMode parseMode) {
        MethodExecutor methodExecutor = Mockito.mock(MethodExecutor.class);
        InlineKeyboardMarkup expectedKeyboard = new InlineKeyboardMarkup();
        new EditTextMessage(methodExecutor, chatId, messageId, text)
                .replyKeyboard(expectedKeyboard)
                .parseMode(parseMode)
                .send();
        var deleteMessageArgumentCaptor = ArgumentCaptor.forClass(EditMessageText.class);
        verify(methodExecutor).execute(deleteMessageArgumentCaptor.capture());
        var actual = deleteMessageArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(chatId.toString(), actual.getChatId()),
                () -> assertEquals(messageId, actual.getMessageId()),
                () -> assertEquals(expectedKeyboard, actual.getReplyMarkup()),
                () -> assertEquals(Objects.nonNull(parseMode) ? parseMode.getValue() : ParseMode.HTML.getValue(), actual.getParseMode())
        );
    }
}