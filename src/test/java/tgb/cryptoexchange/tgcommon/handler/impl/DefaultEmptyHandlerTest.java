package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.tgcommon.service.sender.MessageTypeResolver;
import tgb.cryptoexchange.tgcommon.service.sender.ResponseSender;
import tgb.cryptoexchange.tgcommon.service.sender.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEmptyHandlerTest {

    @Mock
    private ResponseSender responseSender;

    @InjectMocks
    private DefaultEmptyHandler defaultEmptyHandler;

    @ParameterizedTest
    @ValueSource(longs = {123456789L, 987654321L})
    @DisplayName("getEmptyMessage(Long chatId) - вызов - должен вернуть SendMessage с текстом \"Что-то пошло не так\"")
    void shouldSendDefaultMessage(Long chatId) {
        MessageTypeResolver messageTypeResolver = Mockito.mock(MessageTypeResolver.class);
        when(responseSender.to(chatId)).thenReturn(messageTypeResolver);
        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        TextMessage textMessage = Mockito.mock(TextMessage.class);
        when(messageTypeResolver.message(textCaptor.capture())).thenReturn(textMessage);
        defaultEmptyHandler.handle(chatId);
        verify(textMessage).send();
        assertEquals("Что-то пошло не так.", textCaptor.getValue());
    }
}