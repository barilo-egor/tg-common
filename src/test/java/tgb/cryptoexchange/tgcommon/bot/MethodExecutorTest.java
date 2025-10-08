package tgb.cryptoexchange.tgcommon.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodExecutorTest {

    @Mock
    private BotInstance bot;

    @InjectMocks
    private MethodExecutor methodExecutor;

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - передан SendPhoto - вызван bot.execute(SendPhoto sendPhoto)")
    void executeShouldExecuteSendPhoto() throws TelegramApiException {
        Integer messageId = 532552;
        Message message = new Message();
        message.setMessageId(messageId);
        when(bot.execute(any(SendPhoto.class))).thenReturn(message);
        SendPhoto expectedSendPhoto = new SendPhoto();
        expectedSendPhoto.setPhoto(new InputFile("file id"));
        Optional<Message> maybeMessage = methodExecutor.execute(expectedSendPhoto);
        verify(bot).execute(expectedSendPhoto);
        assertTrue(maybeMessage.isPresent());
        assertEquals(maybeMessage.get(), message);
    }

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - передан SendAnimation - вызван bot.execute(SendPhoto sendPhoto)")
    void executeShouldExecuteSendAnimation() throws TelegramApiException {
        Integer messageId = 532552;
        Message message = new Message();
        message.setMessageId(messageId);
        when(bot.execute(any(SendAnimation.class))).thenReturn(message);
        SendAnimation expectedSendAnimation = new SendAnimation();
        expectedSendAnimation.setAnimation(new InputFile("file id"));
        Optional<Message> maybeMessage = methodExecutor.execute(expectedSendAnimation);
        verify(bot).execute(expectedSendAnimation);
        assertTrue(maybeMessage.isPresent());
        assertEquals(maybeMessage.get(), message);
    }

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - передан SendVideo - вызван bot.execute(SendVideo sendVideo)")
    void executeShouldExecuteSendVideo() throws TelegramApiException {
        Integer messageId = 532552;
        Message message = new Message();
        message.setMessageId(messageId);
        when(bot.execute(any(SendVideo.class))).thenReturn(message);
        SendVideo expectedSendVideo = new SendVideo();
        expectedSendVideo.setVideo(new InputFile("file id"));
        Optional<Message> maybeMessage = methodExecutor.execute(expectedSendVideo);
        verify(bot).execute(expectedSendVideo);
        assertTrue(maybeMessage.isPresent());
        assertEquals(maybeMessage.get(), message);
    }

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - передан SendDocument - вызван bot.execute(SendDocument sendDocument)")
    void executeShouldExecuteSendDocument() throws TelegramApiException {
        Integer messageId = 532552;
        Message message = new Message();
        message.setMessageId(messageId);
        when(bot.execute(any(SendDocument.class))).thenReturn(message);
        SendDocument expectedSendDocument = new SendDocument();
        expectedSendDocument.setDocument(new InputFile("file id"));
        Optional<Message> maybeMessage = methodExecutor.execute(expectedSendDocument);
        verify(bot).execute(expectedSendDocument);
        assertTrue(maybeMessage.isPresent());
        assertEquals(maybeMessage.get(), message);
    }

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - передан не поддерживаемый тип - проброшено UnsupportedOperationException")
    void shouldThrowUnsupportedOperationExceptionIfNotSupportedType() {
        SendMediaBotMethod<String> sendMediaBotMethod = new SendMediaBotMethod<String>() {
            @Override
            public String getChatId() {
                return "chatId";
            }

            @Override
            public Integer getMessageThreadId() {
                return 0;
            }

            @Override
            public Integer getReplyToMessageId() {
                return 0;
            }

            @Override
            public Boolean getDisableNotification() {
                return null;
            }

            @Override
            public Boolean getAllowSendingWithoutReply() {
                return null;
            }

            @Override
            public Boolean getProtectContent() {
                return null;
            }

            @Override
            public InputFile getFile() {
                return null;
            }

            @Override
            public String getFileField() {
                return "";
            }

            @Override
            public String deserializeResponse(String answer) throws TelegramApiRequestException {
                return "";
            }

            @Override
            public String getMethod() {
                return "";
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> methodExecutor.execute(sendMediaBotMethod));
    }

    @Test
    @DisplayName("execute(SendMediaBotMethod<?> sendMediaBotMethod) - проброшен TelegramApiException - возвращен пустой Optional")
    void executeShouldReturnEmptyOptionalIfTelegramApiExceptionThrown() throws TelegramApiException {
        when(bot.execute(any(SendPhoto.class))).thenThrow(TelegramApiException.class);
        assertTrue(methodExecutor.execute(SendPhoto.builder().chatId(123L).photo(new InputFile("qwe")).build()).isEmpty());
    }

    @Test
    void executeShouldExecuteBotApiMethodBoolean() throws TelegramApiException {
        AnswerInlineQuery answerInlineQuery = AnswerInlineQuery.builder()
                .inlineQueryId("id")
                .result(InlineQueryResultArticle.builder()
                        .id("id")
                        .title("title")
                        .inputMessageContent(InputTextMessageContent.builder()
                                .messageText("messageText")
                                .build())
                        .description("description")
                        .build()
                ).build();
        methodExecutor.execute(answerInlineQuery);
        verify(bot).execute(answerInlineQuery);
    }

    @Test
    void executeBotApiMethodBooleanShouldSkipTelegramApiException() throws TelegramApiException {
        when(bot.execute(any(AnswerInlineQuery.class))).thenThrow(TelegramApiException.class);
        assertDoesNotThrow(() -> methodExecutor.execute(AnswerInlineQuery.builder()
                .inlineQueryId("id")
                .result(InlineQueryResultArticle.builder()
                        .id("id")
                        .title("title")
                        .inputMessageContent(InputTextMessageContent.builder()
                                .messageText("messageText")
                                .build())
                        .description("description")
                        .build()
                ).build()));
    }

    @Test
    void executeShouldExecuteBotApiMethodSerializable() throws TelegramApiException {
        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(123L)
                .messageId(123)
                .replyMarkup(new InlineKeyboardMarkup())
                .build();
        methodExecutor.execute(editMessageReplyMarkup);
        verify(bot).execute(editMessageReplyMarkup);
    }

    @Test
    void executeBotApiMethodSerializableShouldSkipTelegramApiException() throws TelegramApiException {
        when(bot.execute(any(EditMessageReplyMarkup.class))).thenThrow(TelegramApiException.class);
        assertDoesNotThrow(() -> methodExecutor.execute(EditMessageReplyMarkup.builder()
                .chatId(123L)
                .messageId(123)
                .replyMarkup(new InlineKeyboardMarkup())
                .build()));
    }

    @Test
    void executeShouldExecuteSendMediaGroup() throws TelegramApiException {
        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .chatId(123L)
                .medias(new ArrayList<>())
                .replyToMessageId(123)
                .build();
        methodExecutor.execute(sendMediaGroup);
        verify(bot).execute(sendMediaGroup);
    }

    @Test
    void executeSendMediaGroupShouldSkipTelegramApiException() throws TelegramApiException {
        when(bot.execute(any(SendMediaGroup.class))).thenThrow(TelegramApiException.class);
        assertDoesNotThrow(() -> methodExecutor.execute(SendMediaGroup.builder()
                .chatId(123L)
                .medias(new ArrayList<>())
                .replyToMessageId(123)
                .build()));
    }

    @Test
    void executeShouldExecuteBotApiMethodMessage() throws TelegramApiException {
        Message message = new Message();
        message.setMessageId(555);
        when(bot.execute(any(SendMessage.class))).thenReturn(message);
        SendMessage sendMessage = SendMessage
                .builder()
                .text("text")
                .chatId(123L)
                .build();
        methodExecutor.execute(sendMessage);
        verify(bot).execute(sendMessage);
    }

    @Test
    void executeBotApiMethodMessageShouldSkipTelegramApiException() throws TelegramApiException {
        when(bot.execute(any(SendMessage.class))).thenThrow(TelegramApiException.class);
        assertDoesNotThrow(() -> methodExecutor.execute(SendMessage
                .builder()
                .text("text")
                .chatId(123L)
                .build()));
    }
}