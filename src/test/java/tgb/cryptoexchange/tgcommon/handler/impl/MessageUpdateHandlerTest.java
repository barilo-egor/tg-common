package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.MessageType;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageUpdateHandlerTest {

    @Test
    @DisplayName("handle(Update update) - отсутствует обработчик для MessageType.MESSAGE - возвращен false")
    void shouldReturnFalseIfNoHandler() {
        Update update = new Update();
        Message message = new Message();
        message.setText("some text");
        update.setMessage(message);

        List<MessageHandler> messageHandlers = new ArrayList<>();
        MessageHandler photoMessageHandler = Mockito.mock(MessageHandler.class);
        when(photoMessageHandler.getMessageType()).thenReturn(MessageType.PHOTO);
        messageHandlers.add(photoMessageHandler);
        MessageHandler videoMessageHandler = Mockito.mock(MessageHandler.class);
        when(videoMessageHandler.getMessageType()).thenReturn(MessageType.VIDEO);
        messageHandlers.add(videoMessageHandler);
        MessageUpdateHandler messageUpdateHandler = new MessageUpdateHandler(messageHandlers);
        assertFalse(messageUpdateHandler.handle(update));
    }


    @Test
    @DisplayName("handle(Update update) - присутствует обработчик для MessageType.MESSAGE - возвращен true")
    void shouldReturnTrueIfHasHandler() {
        Update update = new Update();
        Message message = new Message();
        message.setText("some text");
        update.setMessage(message);

        List<MessageHandler> messageHandlers = new ArrayList<>();
        MessageHandler photoMessageHandler = Mockito.mock(MessageHandler.class);
        when(photoMessageHandler.getMessageType()).thenReturn(MessageType.PHOTO);
        messageHandlers.add(photoMessageHandler);
        MessageHandler videoMessageHandler = Mockito.mock(MessageHandler.class);
        when(videoMessageHandler.getMessageType()).thenReturn(MessageType.VIDEO);
        messageHandlers.add(videoMessageHandler);
        MessageHandler textMessageHandler = Mockito.mock(MessageHandler.class);
        when(textMessageHandler.getMessageType()).thenReturn(MessageType.TEXT);
        when(textMessageHandler.handleMessage(message)).thenReturn(true);
        messageHandlers.add(textMessageHandler);
        MessageUpdateHandler messageUpdateHandler = new MessageUpdateHandler(messageHandlers);
        assertTrue(messageUpdateHandler.handle(update));
    }

    @Test
    @DisplayName("getUpdateType() - вызов - должен вернуть UpdateType.MESSAGE")
    void shouldReturnMESSAGE() {
        assertEquals(UpdateType.MESSAGE, new MessageUpdateHandler(new ArrayList<>()).getUpdateType());
    }
}