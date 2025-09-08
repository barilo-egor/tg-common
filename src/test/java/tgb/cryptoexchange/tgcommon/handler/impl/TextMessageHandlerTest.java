package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.MessageType;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;
import tgb.cryptoexchange.tgcommon.handler.TextHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextMessageHandlerTest {

    @Test
    @DisplayName("handleMessage(Message message) - отсутствует подходящий обработчик - возвращен false")
    void handleMessageShouldReturnFalseIfNoHandler() {
        List<TextHandler> textHandlers = new ArrayList<>();
        TextHandler textCommandTextHandler = Mockito.mock(TextHandler.class);
        when(textCommandTextHandler.getTextMessageType()).thenReturn(TextMessageType.TEXT_COMMAND);
        textHandlers.add(textCommandTextHandler);
        TextMessageHandler textMessageHandler = new TextMessageHandler(textHandlers);

        Message message = new Message();
        message.setText("/start");

        assertFalse(textMessageHandler.handleMessage(message));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("handleMessage(Message message) - подходящий обработчик есть - возвращен результат метода handle обработчика")
    void shouldReturnResultOfMethodHandleOfHandler(boolean handleResult) {
        List<TextHandler> textHandlers = new ArrayList<>();
        TextHandler textCommandTextHandler = Mockito.mock(TextHandler.class);
        when(textCommandTextHandler.getTextMessageType()).thenReturn(TextMessageType.TEXT_COMMAND);
        when(textCommandTextHandler.handle(Mockito.any())).thenReturn(handleResult);
        textHandlers.add(textCommandTextHandler);
        TextMessageHandler textMessageHandler = new TextMessageHandler(textHandlers);

        Message message = new Message();
        message.setText("Настройки");

        assertEquals(handleResult, textMessageHandler.handleMessage(message));
    }

    @Test
    @DisplayName("getMessageType() - вызов - возвращен MessageType.TEXT")
    void shouldReturnTEXT() {
        assertEquals(MessageType.TEXT, new TextMessageHandler(new ArrayList<>()).getMessageType());
    }
}