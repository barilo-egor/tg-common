package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgb.cryptoexchange.tgcommon.bot.BotInstance;
import tgb.cryptoexchange.tgcommon.keyboard.KeyboardBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseSenderTest {

    @Mock
    private BotInstance bot;

    @Mock
    private KeyboardBuilder keyboardBuilder;

    @InjectMocks
    private ResponseSender responseSender;

    @ParameterizedTest
    @CsvSource(nullValues = {"null"}, value = {
            "123456789,some text,12353,html",
            "987654321,another text,1,markdown",
            "987654322,simple message text,null,null"
    })
    @DisplayName("sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, Integer replyToMessageId, String parseMode) " +
            "- должен вернуть отправленный месседж с переданными параметрами")
    void sendMessageShouldReturnMessageWithPassedParameters(Long chatId, String text, Integer replyToMessageId, String parseMode) throws TelegramApiException {
        Message message = new Message();
        when(bot.execute(any(SendMessage.class))).thenReturn(message);
        ReplyKeyboard expectedKeyboard = new ReplyKeyboardMarkup();
        responseSender.sendMessage(chatId, text, expectedKeyboard, replyToMessageId, parseMode);
        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(sendMessageArgumentCaptor.capture());
        SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(chatId.toString(), sendMessage.getChatId()),
                () -> assertEquals(text, sendMessage.getText()),
                () -> assertEquals(expectedKeyboard, sendMessage.getReplyMarkup()),
                () -> assertEquals(replyToMessageId, sendMessage.getReplyToMessageId()),
                () -> assertEquals(parseMode, sendMessage.getParseMode())
        );
    }

    @Test
    @DisplayName("sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, Integer replyToMessageId, String parseMode) " +
            "- execute пробросил TelegramApiException - возвращен пустой Optional")
    void sendMessageShouldReturnEmptyOptionalIfTelegramApiExceptionThrown() throws TelegramApiException {
        when(bot.execute(any(SendMessage.class))).thenThrow(TelegramApiException.class);
        assertTrue(responseSender.sendMessage(123456789L, "text", null, null, "html").isEmpty());
    }
}