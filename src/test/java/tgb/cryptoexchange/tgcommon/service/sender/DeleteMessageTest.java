package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteMessageTest {

    @ParameterizedTest
    @CsvSource({
            "12354323,453345",
            "123424323525,123"
    })
    void shouldCreateWithPassedParameters(Long chatId, Integer messageId) {
        MethodExecutor methodExecutor = Mockito.mock(MethodExecutor.class);
        new DeleteMessage(methodExecutor, chatId)
                .messageId(messageId)
                .delete();
        var deleteMessageArgumentCaptor = ArgumentCaptor.forClass(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.class);
        verify(methodExecutor).execute(deleteMessageArgumentCaptor.capture());
        var actual = deleteMessageArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(chatId.toString(), actual.getChatId()),
                () -> assertEquals(messageId, actual.getMessageId())
        );
    }
}