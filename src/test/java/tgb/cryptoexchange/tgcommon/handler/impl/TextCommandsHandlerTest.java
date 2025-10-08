package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;
import tgb.cryptoexchange.tgcommon.handler.TextCommandHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextCommandsHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = {"Настройки", "Контакты", "123"})
    @DisplayName("handle(Message message) - обработчик для команды отсутствует - возвращен false")
    void shouldReturnFalseIfNoHandler() {
        Message message = new Message();
        message.setText("Несуществующая команда");

        List<TextCommandHandler> slashCommandHandlers = new ArrayList<>();
        TextCommandHandler commandHandler = Mockito.mock(TextCommandHandler.class);
        when(commandHandler.getTextCommand()).thenReturn("Команда");
        slashCommandHandlers.add(commandHandler);
        TextCommandHandler textCommandHandler = Mockito.mock(TextCommandHandler.class);
        when(textCommandHandler.getTextCommand()).thenReturn("Текстовая команда");
        slashCommandHandlers.add(textCommandHandler);
        TextCommandsHandler slashCommandsHandler = new TextCommandsHandler(slashCommandHandlers);
        assertFalse(slashCommandsHandler.handle(message));
    }

    @Test
    @DisplayName("handle(Message message) - отсутствует доступ к обработчику - возвращен false")
    void shouldReturnFalseIfHasNoAccess() {
        Long chatId = 123456789L;
        Message message = new Message();
        message.setText("Команда");
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        List<TextCommandHandler> slashCommandHandlers = new ArrayList<>();
        TextCommandHandler commandHandler = Mockito.mock(TextCommandHandler.class);
        when(commandHandler.getTextCommand()).thenReturn("Команда");
        slashCommandHandlers.add(commandHandler);
        when(commandHandler.hasAccess(chatId)).thenReturn(false);
        TextCommandHandler textCommandHandler = Mockito.mock(TextCommandHandler.class);
        when(textCommandHandler.getTextCommand()).thenReturn("Текстовая команда");
        slashCommandHandlers.add(textCommandHandler);
        TextCommandsHandler slashCommandsHandler = new TextCommandsHandler(slashCommandHandlers);
        assertFalse(slashCommandsHandler.handle(message));
    }

    @Test
    @DisplayName("handle(Message message) - есть доступ к обработчику - возвращен true")
    void shouldReturnTrueIfHasAccess() {
        Long chatId = 123456789L;
        Message message = new Message();
        message.setText("Команда");
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        List<TextCommandHandler> slashCommandHandlers = new ArrayList<>();
        TextCommandHandler commandHandler = Mockito.mock(TextCommandHandler.class);
        when(commandHandler.getTextCommand()).thenReturn("Команда");
        slashCommandHandlers.add(commandHandler);
        when(commandHandler.hasAccess(chatId)).thenReturn(true);
        TextCommandHandler textCommandHandler = Mockito.mock(TextCommandHandler.class);
        when(textCommandHandler.getTextCommand()).thenReturn("Текстовая команда");
        slashCommandHandlers.add(textCommandHandler);
        TextCommandsHandler slashCommandsHandler = new TextCommandsHandler(slashCommandHandlers);
        assertTrue(slashCommandsHandler.handle(message));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(commandHandler).handle(messageArgumentCaptor.capture());
        assertEquals(message, messageArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("getTextMessageType() - вызов - возвращен TextMessageType.TEXT_COMMAND")
    void shouldReturnSLASH_COMMAND() {
        assertEquals(TextMessageType.TEXT_COMMAND, new TextCommandsHandler(new ArrayList<>()).getTextMessageType());
    }
}