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
import tgb.cryptoexchange.tgcommon.handler.SlashCommandHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlashCommandsHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = {"qwe", "/123", "/asd123"})
    @DisplayName("handle(Message message) - обработчик для команды отсутствует - возвращен false")
    void shouldReturnFalseIfNoHandler() {
        Message message = new Message();
        message.setText("/someCommand");

        List<SlashCommandHandler> slashCommandHandlers = new ArrayList<>();
        SlashCommandHandler startSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(startSlashCommandHandler.getSlashCommand()).thenReturn("/start");
        slashCommandHandlers.add(startSlashCommandHandler);
        SlashCommandHandler helpSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(helpSlashCommandHandler.getSlashCommand()).thenReturn("help");
        slashCommandHandlers.add(helpSlashCommandHandler);
        SlashCommandsHandler slashCommandsHandler = new SlashCommandsHandler(slashCommandHandlers);
        assertFalse(slashCommandsHandler.handle(message));
    }

    @Test
    @DisplayName("handle(Message message) - отсутствует доступ к обработчику - возвращен false")
    void shouldReturnFalseIfHasNoAccess() {
        Long chatId = 123456789L;
        Message message = new Message();
        message.setText("/help");
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        List<SlashCommandHandler> slashCommandHandlers = new ArrayList<>();
        SlashCommandHandler startSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(startSlashCommandHandler.getSlashCommand()).thenReturn("/start");
        slashCommandHandlers.add(startSlashCommandHandler);
        SlashCommandHandler helpSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(helpSlashCommandHandler.getSlashCommand()).thenReturn("help");
        when(helpSlashCommandHandler.hasAccess(chatId)).thenReturn(false);
        slashCommandHandlers.add(helpSlashCommandHandler);
        SlashCommandsHandler slashCommandsHandler = new SlashCommandsHandler(slashCommandHandlers);
        assertFalse(slashCommandsHandler.handle(message));
    }

    @Test
    @DisplayName("handle(Message message) - есть доступ к обработчику - возвращен true")
    void shouldReturnTrueIfHasAccess() {
        Long chatId = 123456789L;
        Message message = new Message();
        message.setText("/start");
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        List<SlashCommandHandler> slashCommandHandlers = new ArrayList<>();
        SlashCommandHandler startSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(startSlashCommandHandler.getSlashCommand()).thenReturn("/start");
        when(startSlashCommandHandler.hasAccess(chatId)).thenReturn(true);
        slashCommandHandlers.add(startSlashCommandHandler);
        SlashCommandHandler helpSlashCommandHandler = Mockito.mock(SlashCommandHandler.class);
        when(helpSlashCommandHandler.getSlashCommand()).thenReturn("help");
        slashCommandHandlers.add(helpSlashCommandHandler);
        SlashCommandsHandler slashCommandsHandler = new SlashCommandsHandler(slashCommandHandlers);
        assertTrue(slashCommandsHandler.handle(message));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(startSlashCommandHandler).handle(messageArgumentCaptor.capture());
        assertEquals(message, messageArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("getTextMessageType() - вызов - возвращен TextMessageType.SLASH_COMMAND")
    void shouldReturnSLASH_COMMAND() {
        assertEquals(TextMessageType.SLASH_COMMAND, new SlashCommandsHandler(new ArrayList<>()).getTextMessageType());
    }
}