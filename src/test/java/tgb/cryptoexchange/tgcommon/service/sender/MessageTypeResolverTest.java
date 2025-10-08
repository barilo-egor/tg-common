package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageTypeResolverTest {

    @Mock
    private MethodExecutor methodExecutor;

    private static final Long CHAT_ID = 123456789L;

    private MessageTypeResolver messageTypeResolver;

    @BeforeEach
    void setUp() {
        messageTypeResolver = new MessageTypeResolver(methodExecutor, CHAT_ID);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "qwetwe",
            "some text"
    })
    void messageShouldCreateWithPassedText(String text) {
        TextMessage textMessage = messageTypeResolver.message(text);
        textMessage.send();
        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(methodExecutor).execute(sendMessageArgumentCaptor.capture());
        assertEquals(text, sendMessageArgumentCaptor.getValue().getText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE"
    })
    void photoShouldCreateWithPassedFileId(String fileId) {
        PhotoMessage photoMessage = messageTypeResolver.photo(new InputFile(fileId));
        photoMessage.send();
        ArgumentCaptor<SendPhoto> sendPhotoArgumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        verify(methodExecutor).execute(sendPhotoArgumentCaptor.capture());
        assertEquals(fileId, sendPhotoArgumentCaptor.getValue().getPhoto().getAttachName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE"
    })
    void animationShouldCreateWithPassedFileId(String fileId) {
        AnimationMessage animationMessage = messageTypeResolver.animation(new InputFile(fileId));
        animationMessage.send();
        ArgumentCaptor<SendAnimation> sendAnimationArgumentCaptor = ArgumentCaptor.forClass(SendAnimation.class);
        verify(methodExecutor).execute(sendAnimationArgumentCaptor.capture());
        assertEquals(fileId, sendAnimationArgumentCaptor.getValue().getAnimation().getAttachName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE"
    })
    void documentShouldCreateWithPassedFileId(String fileId) {
        DocumentMessage documentMessage = messageTypeResolver.document(new InputFile(fileId));
        documentMessage.send();
        ArgumentCaptor<SendDocument> sendDocumentArgumentCaptor = ArgumentCaptor.forClass(SendDocument.class);
        verify(methodExecutor).execute(sendDocumentArgumentCaptor.capture());
        assertEquals(fileId, sendDocumentArgumentCaptor.getValue().getDocument().getAttachName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE"
    })
    void videoShouldCreateWithPassedFileId(String fileId) {
        VideoMessage videoMessage = messageTypeResolver.video(new InputFile(fileId));
        videoMessage.send();
        ArgumentCaptor<SendVideo> sendVideoArgumentCaptor = ArgumentCaptor.forClass(SendVideo.class);
        verify(methodExecutor).execute(sendVideoArgumentCaptor.capture());
        assertEquals(fileId, sendVideoArgumentCaptor.getValue().getVideo().getAttachName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ",
            "AgACAgIAAxkBAAIK9GLbnunx5qF7ZrTryJScUhoYxmrLAAKDuzEbtDvhSmFh-KZkOSY8AQADAgADeQADKQQ," +
                    "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE",
            "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE," +
                    "AgACAgIAAxkBAAILgmLiOowWC6YRhs4AASW5uf9vLZXM6gACOL4xG8_QGEsO9UY0qAEGWwEAAwIAA20AAykE," +
                    "AgACAgIAAxkBAAIHhGLWeZ5YLcbhmJPOasQY5Bg1Xjl0AAKUwDEb-aKxSiWKBbH7AzL_AQADAgADeQADKQQ"
    })
    void mediaGroupShouldCreateWithPassedFileIds(String fileIdsStr) {
        List<String> fileIds = Arrays.stream(fileIdsStr.split(",")).toList();
        List<InputMedia> inputMedia = new ArrayList<>();
        for (String fileId : fileIds) {
            inputMedia.add(new InputMediaPhoto(fileId));
        }
        Integer expectedReplyMessageId = 123532;
        messageTypeResolver.mediaGroup(inputMedia).replyToMessageId(expectedReplyMessageId).send();
        ArgumentCaptor<SendMediaGroup> sendMediaGroupArgumentCaptor = ArgumentCaptor.forClass(SendMediaGroup.class);
        verify(methodExecutor).execute(sendMediaGroupArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(fileIds.size(), sendMediaGroupArgumentCaptor.getValue().getMedias().size()),
                () -> assertEquals(inputMedia, sendMediaGroupArgumentCaptor.getValue().getMedias()),
                () -> assertEquals(expectedReplyMessageId, sendMediaGroupArgumentCaptor.getValue().getReplyToMessageId())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1,1",
            "534534,qwetwe",
            "63463454,some text"
    })
    void editTextShouldCreateWithPassedTextAndMessageId(Integer messageId, String text) {
        EditTextMessage editTextMessage = messageTypeResolver.editText(messageId, text);
        editTextMessage.send();
        ArgumentCaptor<EditMessageText> editMessageTextArgumentCaptor = ArgumentCaptor.forClass(EditMessageText.class);
        verify(methodExecutor).execute(editMessageTextArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(text, editMessageTextArgumentCaptor.getValue().getText()),
                () -> assertEquals(messageId, editMessageTextArgumentCaptor.getValue().getMessageId())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "534534",
            "63463454"
    })
    void editKeyboardShouldCreateWithPassedMessageId(Integer messageId) {
        InlineKeyboardMarkup expectedKeyboard = new InlineKeyboardMarkup();
        EditMessageReplyKeyboard editTextMessage = messageTypeResolver.editKeyboard(messageId, expectedKeyboard);
        editTextMessage.send();
        ArgumentCaptor<EditMessageReplyMarkup> editMessageReplyMarkupArgumentCaptor = ArgumentCaptor.forClass(EditMessageReplyMarkup.class);
        verify(methodExecutor).execute(editMessageReplyMarkupArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(messageId, editMessageReplyMarkupArgumentCaptor.getValue().getMessageId()),
                () -> assertEquals(expectedKeyboard, editMessageReplyMarkupArgumentCaptor.getValue().getReplyMarkup())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "534534",
            "63463454"
    })
    void deleteShouldCreateWithPassedMessageId(Integer messageId) {
        messageTypeResolver.delete(messageId);
        ArgumentCaptor<org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage> deleteMessageArgumentCaptor =
                ArgumentCaptor.forClass(org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage.class);
        verify(methodExecutor).execute(deleteMessageArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(messageId, deleteMessageArgumentCaptor.getValue().getMessageId())
        );
    }

    @Test
    void actionTest() {
        Action deleteMessage = messageTypeResolver.action();
        deleteMessage.typing();
        verify(methodExecutor).execute(any(SendChatAction.class));
    }
}