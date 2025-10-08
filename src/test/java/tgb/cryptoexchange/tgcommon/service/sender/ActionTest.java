package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActionTest {

    @Mock
    private MethodExecutor methodExecutor;

    @ParameterizedTest
    @ValueSource(longs = {
            123456789L, 987654321L, 543120987L
    })
    void typingShouldCreateSendChatActionWithTypingAndPassedChatId(Long chatId) {
        Action action = new Action(methodExecutor, chatId);
        action.typing();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals("typing", actual.getAction()),
                () -> assertEquals(chatId.toString(), actual.getChatId())
        );
    }

    @Test
    void chooseStickerShouldCreateSendChatActionWithChooseSticker() {
        Action action = new Action(methodExecutor, 123456789L);
        action.chooseSticker();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("choose_sticker", actual.getAction());
    }

    @Test
    void uploadPhotoShouldCreateSendChatActionWithUploadPhoto() {
        Action action = new Action(methodExecutor, 123456789L);
        action.uploadPhoto();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("upload_photo", actual.getAction());
    }

    @Test
    void uploadVideoShouldCreateSendChatActionWithUploadVideo() {
        Action action = new Action(methodExecutor, 123456789L);
        action.uploadVideo();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("upload_video", actual.getAction());
    }

    @Test
    void uploadDocumentShouldCreateSendChatActionWithUploadDocument() {
        Action action = new Action(methodExecutor, 123456789L);
        action.uploadDocument();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("upload_document", actual.getAction());
    }

    @Test
    void uploadVoiceShouldCreateSendChatActionWithUploadVoice() {
        Action action = new Action(methodExecutor, 123456789L);
        action.uploadVoice();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("upload_voice", actual.getAction());
    }

    @Test
    void uploadVideoNoteShouldCreateSendChatActionWithUploadVideoNote() {
        Action action = new Action(methodExecutor, 123456789L);
        action.uploadVideoNote();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("upload_video_note", actual.getAction());
    }

    @Test
    void recordVideoShouldCreateSendChatActionWithRecordVideo() {
        Action action = new Action(methodExecutor, 123456789L);
        action.recordVideo();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("record_video", actual.getAction());
    }

    @Test
    void recordVoiceShouldCreateSendChatActionWithRecordVoice() {
        Action action = new Action(methodExecutor, 123456789L);
        action.recordVoice();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("record_voice", actual.getAction());
    }

    @Test
    void recordVideoNoteShouldCreateSendChatActionWithRecordVideoNote() {
        Action action = new Action(methodExecutor, 123456789L);
        action.recordVideoNote();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("record_video_note", actual.getAction());
    }

    @Test
    void findLocationShouldCreateSendChatActionWithFindLocation() {
        Action action = new Action(methodExecutor, 123456789L);
        action.findLocation();
        ArgumentCaptor<SendChatAction> sendChatActionArgumentCaptor = ArgumentCaptor.forClass(SendChatAction.class);
        verify(methodExecutor).execute(sendChatActionArgumentCaptor.capture());
        SendChatAction actual = sendChatActionArgumentCaptor.getValue();
        assertEquals("find_location", actual.getAction());
    }
}