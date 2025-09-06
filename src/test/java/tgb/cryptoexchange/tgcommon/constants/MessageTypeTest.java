package tgb.cryptoexchange.tgcommon.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageTypeTest {

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен TEXT")
    void shouldReturnTEXTFromMessageWithText() {
        Message message = new Message();
        message.setText("text");
        assertEquals(MessageType.TEXT, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен PHOTO")
    void shouldReturnPHOTOFromMessageWithPhoto() {
        Message message = new Message();
        List<PhotoSize> photos = new ArrayList<>();
        photos.add(new PhotoSize());
        message.setPhoto(photos);
        assertEquals(MessageType.PHOTO, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен VIDEO")
    void shouldReturnVIDEOFromMessageWithVideo() {
        Message message = new Message();
        message.setVideo(new Video());
        assertEquals(MessageType.VIDEO, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен AUDIO")
    void shouldReturnAUDIOFromMessageWithVideo() {
        Message message = new Message();
        message.setAudio(new Audio());
        assertEquals(MessageType.AUDIO, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен DOCUMENT")
    void shouldReturnDOCUMENTFromMessageWithDocument() {
        Message message = new Message();
        message.setDocument(new Document());
        assertEquals(MessageType.DOCUMENT, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен STICKER")
    void shouldReturnSTICKERFromMessageWithSticker() {
        Message message = new Message();
        message.setSticker(new Sticker());
        assertEquals(MessageType.STICKER, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен ANIMATION")
    void shouldReturnANIMATIONFromMessageWithAnimation() {
        Message message = new Message();
        message.setAnimation(new Animation());
        assertEquals(MessageType.ANIMATION, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен VOICE")
    void shouldReturnVoiceFromMessageWithVoice() {
        Message message = new Message();
        message.setVoice(new Voice());
        assertEquals(MessageType.VOICE, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен VIDEO_NOTE")
    void shouldReturnVIDEOFromMessageWithVideoNote() {
        Message message = new Message();
        message.setVideoNote(new VideoNote());
        assertEquals(MessageType.VIDEO_NOTE, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен CONTACT")
    void shouldReturnCONTACTFromMessageWithContact() {
        Message message = new Message();
        message.setContact(new Contact());
        assertEquals(MessageType.CONTACT, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен LOCATION")
    void shouldReturnLOCATIONFromMessageWithLocation() {
        Message message = new Message();
        message.setLocation(new Location());
        assertEquals(MessageType.LOCATION, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен VENUE")
    void shouldReturnVENUEFromMessageWithVenue() {
        Message message = new Message();
        message.setVenue(new Venue());
        assertEquals(MessageType.VENUE, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен POLL")
    void shouldReturnPOLLFromMessageWithPoll() {
        Message message = new Message();
        message.setPoll(new Poll());
        assertEquals(MessageType.POLL, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - месседж с текстом - возвращен DICE")
    void shouldReturnDICEFromMessageWithDice() {
        Message message = new Message();
        message.setDice(new Dice());
        assertEquals(MessageType.DICE, MessageType.fromMessage(message));
    }

    @Test
    @DisplayName("fromMessage(Message message) - пустой месседж - проброшен IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionIfEmptyMessage() {
        Message message = new Message();
        assertThrows(IllegalArgumentException.class, () -> MessageType.fromMessage(message));
    }
}