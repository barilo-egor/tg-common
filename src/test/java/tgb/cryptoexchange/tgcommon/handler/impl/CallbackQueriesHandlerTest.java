package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.handler.CallbackQueryHandler;
import tgb.cryptoexchange.tgcommon.keyboard.PressedInlineButton;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallbackQueriesHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = {"3", "4:123", "qweasd123:asdqwe", ""})
    @DisplayName("handle(Update update) - передан id на которого нет обработчика - возвращен false")
    void shouldReturnFalseIfNoHandlerForId(String callbackData) {
        CallbackQueryHandler handler1 = Mockito.mock(CallbackQueryHandler.class);
        when(handler1.getId()).thenReturn("1");
        CallbackQueryHandler handler2 = Mockito.mock(CallbackQueryHandler.class);
        when(handler2.getId()).thenReturn("2");
        List<CallbackQueryHandler> callbackQueryHandlers = new ArrayList<>();
        callbackQueryHandlers.add(handler1);
        callbackQueryHandlers.add(handler2);
        CallbackQueriesHandler callbackQueriesHandler = new CallbackQueriesHandler(callbackQueryHandlers);

        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData(callbackData);
        update.setCallbackQuery(callbackQuery);

        assertFalse(callbackQueriesHandler.handle(update));
    }

    @Test
    @DisplayName("handle(Update update) - у пользователя нет доступа к обработчику - возвращен false")
    void shouldReturnFalseIfHasNoAccess() {
        CallbackQueryHandler handler = Mockito.mock(CallbackQueryHandler.class);
        when(handler.getId()).thenReturn("1");
        List<CallbackQueryHandler> callbackQueryHandlers = new ArrayList<>();
        callbackQueryHandlers.add(handler);
        CallbackQueriesHandler callbackQueriesHandler = new CallbackQueriesHandler(callbackQueryHandlers);

        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message = new Message();
        Chat chat = new Chat();
        Long chatId = 123456789L;
        chat.setId(chatId);
        message.setChat(chat);
        callbackQuery.setMessage(message);
        callbackQuery.setData("1");
        update.setCallbackQuery(callbackQuery);

        when(handler.hasAccess(chatId)).thenReturn(false);
        assertFalse(callbackQueriesHandler.handle(update));
    }

    @Test
    @DisplayName("handle(Update update) - у пользователя есть доступ к обработчику - возвращен true")
    void shouldReturnTrueIfHasAccess() {
        CallbackQueryHandler handler = Mockito.mock(CallbackQueryHandler.class);
        List<CallbackQueryHandler> callbackQueryHandlers = new ArrayList<>();
        when(handler.getId()).thenReturn("1");
        callbackQueryHandlers.add(handler);
        CallbackQueriesHandler callbackQueriesHandler = new CallbackQueriesHandler(callbackQueryHandlers);

        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message = new Message();
        Chat chat = new Chat();
        Long chatId = 123456789L;
        chat.setId(chatId);
        message.setChat(chat);
        callbackQuery.setMessage(message);
        String callbackData = "1:qwe:123";
        callbackQuery.setData(callbackData);
        String callbackQueryId = "someId";
        callbackQuery.setId(callbackQueryId);
        update.setCallbackQuery(callbackQuery);

        when(handler.hasAccess(chatId)).thenReturn(true);
        assertTrue(callbackQueriesHandler.handle(update));
        ArgumentCaptor<PressedInlineButton> buttonArgumentCaptor = ArgumentCaptor.forClass(PressedInlineButton.class);
        verify(handler).handle(buttonArgumentCaptor.capture());
        PressedInlineButton pressedInlineButton = buttonArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(callbackQueryId, pressedInlineButton.getCallbackQueryId()),
                () -> assertEquals(callbackData, pressedInlineButton.getData()),
                () -> assertEquals(message, pressedInlineButton.getMessage())
        );
    }

    @Test
    @DisplayName("getUpdateType() - вызов - должен вернуть UpdateType.CALLBACK_QUERY")
    void shouldReturnCALLBACK_QUERY() {
        CallbackQueriesHandler callbackQueriesHandler = new CallbackQueriesHandler(new ArrayList<>());
        assertEquals(UpdateType.CALLBACK_QUERY, callbackQueriesHandler.getUpdateType());
    }
}