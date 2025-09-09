package tgb.cryptoexchange.tgcommon.keyboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KeyboardBuilderTest {

    private final KeyboardBuilder keyboardBuilder = new KeyboardBuilder();

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10})
    void buildInlineShouldCreateKeyboardWithOneColumn(int numberOfButtons) {
        List<InlineButton> buttons = new ArrayList<>(numberOfButtons);
        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(new InlineButton("some id " + i, "some text " + i));
        }
        InlineKeyboardMarkup actualKeyboard = keyboardBuilder.buildInline(buttons);
        assertEquals(numberOfButtons, actualKeyboard.getKeyboard().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldThrowTelegramCommonExceptionIfNotValidMaxNumberOfColumns(int maxNumberOfColumns) {
        Executable executable = () -> keyboardBuilder.buildInline(
                maxNumberOfColumns,
                List.of(new InlineButton("id","text"))
        );
        assertThrows(TelegramCommonException.class, executable);
    }

    @Test
    void buildInlineShouldThrowTelegramCommonExceptionIfEmptyButtons() {
        Executable executable = () -> keyboardBuilder.buildInline(2, new ArrayList<>());
        assertThrows(TelegramCommonException.class, executable);
    }

    @Test
    void buildInlineShouldThrowTelegramCommonExceptionIfNullButtons() {
        Executable executable = () -> keyboardBuilder.buildInline(2, null);
        assertThrows(TelegramCommonException.class, executable);
    }

    @ParameterizedTest
    @CsvSource({
            "5,3,2",
            "1,2,1",
            "2,2,1",
            "3,2,2",
            "8,1,8"
    })
    void buildInlineShouldCreateKeyboardWithPassedParameterNumberOfColumns(int numberOfButtons, int maxNumberOfColumns, int expectedRowsNumber) {
        List<InlineButton> buttons = new ArrayList<>();
        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(new InlineButton("id " + i, "text " + i));
        }

        InlineKeyboardMarkup actualKeyboard = keyboardBuilder.buildInline(maxNumberOfColumns, buttons);
        assertEquals(expectedRowsNumber, actualKeyboard.getKeyboard().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 6})
    void buildInlineByRowsShouldCreatePassedNumberOfRows(int numberOfRows) {
        List<List<InlineButton>> rows = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            List<InlineButton> row = new ArrayList<>();
            row.add(new InlineButton("id " + i, "text " + i));
            rows.add(row);
        }
        InlineKeyboardMarkup actualKeyboard = keyboardBuilder.buildInlineByRows(rows);
        assertEquals(numberOfRows, actualKeyboard.getKeyboard().size());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,1",
            "1,2,1",
            "3,2,1",
            "4,2,5"
    })
    void buildInlineByRowsShouldCreatePassedNumberOfRows(int firstRowButtonsNumber, int secondRowButtonsNumber, int thirdRowButtonsNumber) {
        List<List<InlineButton>> rows = new ArrayList<>();
        List<InlineButton> row1 = new ArrayList<>();
        for (int i = 0; i < firstRowButtonsNumber; i++) {
            row1.add(new InlineButton("id " + i, "text " + i));
        }
        rows.add(row1);
        List<InlineButton> row2 = new ArrayList<>();
        for (int i = 0; i < secondRowButtonsNumber; i++) {
            row2.add(new InlineButton("id " + i, "text " + i));
        }
        rows.add(row2);
        List<InlineButton> row3 = new ArrayList<>();
        for (int i = 0; i < thirdRowButtonsNumber; i++) {
            row3.add(new InlineButton("id " + i, "text " + i));
        }
        rows.add(row3);
        InlineKeyboardMarkup actualKeyboard = keyboardBuilder.buildInlineByRows(rows);
        List<InlineKeyboardButton> actualRow1 = actualKeyboard.getKeyboard().getFirst();
        List<InlineKeyboardButton> actualRow2 = actualKeyboard.getKeyboard().get(1);
        List<InlineKeyboardButton> actualRow3 = actualKeyboard.getKeyboard().get(2);
        assertAll(
                () -> assertEquals(firstRowButtonsNumber, actualRow1.size()),
                () -> assertEquals(secondRowButtonsNumber, actualRow2.size()),
                () -> assertEquals(thirdRowButtonsNumber, actualRow3.size())
        );
    }

    @Test
    void buildInlineShouldParseButtonByInlineType() {
        List<InlineButton> buttons = new ArrayList<>();
        String url = "https://example.com";
        buttons.add(new InlineButton("URL", InlineButton.InlineType.URL, url));
        String callbackDataHandlerId = "callbackDataHandlerId";
        buttons.add(new InlineButton(callbackDataHandlerId, "CALLBACK_DATA"));
        String switchInlineQueryData = "SIQ data";
        buttons.add(new InlineButton("SWITCH_INLINE_QUERY", InlineButton.InlineType.SWITCH_INLINE_QUERY, switchInlineQueryData));
        String switchInlineQueryCurrentChatData = "SIQCC data";
        buttons.add(new InlineButton("SWITCH_INLINE_QUERY_CURRENT_CHAT",
                InlineButton.InlineType.SWITCH_INLINE_QUERY_CURRENT_CHAT, switchInlineQueryCurrentChatData));
        String webApp = "https://google.com";
        buttons.add(new InlineButton("WEB_APP", InlineButton.InlineType.WEB_APP, webApp));
        String nullInlineTypeData = "some data";
        buttons.add(new InlineButton("CALLBACK_DATA", null, nullInlineTypeData));
        InlineKeyboardMarkup actualKeyboard = keyboardBuilder.buildInline(1, buttons);
        assertAll(
                () -> assertEquals(actualKeyboard.getKeyboard().getFirst().getFirst().getUrl(), url),
                () -> assertEquals(actualKeyboard.getKeyboard().get(1).getFirst().getCallbackData(), callbackDataHandlerId),
                () -> assertEquals(actualKeyboard.getKeyboard().get(2).getFirst().getSwitchInlineQuery(), switchInlineQueryData),
                () -> assertEquals(actualKeyboard.getKeyboard().get(3).getFirst().getSwitchInlineQueryCurrentChat(), switchInlineQueryCurrentChatData),
                () -> assertEquals(actualKeyboard.getKeyboard().get(4).getFirst().getWebApp().getUrl(), webApp),
                () -> assertEquals(actualKeyboard.getKeyboard().get(5).getFirst().getCallbackData(), nullInlineTypeData)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10})
    void buildReplyShouldCreateKeyboardWithOneColumnNotOneTimeAndResize(int numberOfButtons) {
        List<ReplyButton> buttons = new ArrayList<>(numberOfButtons);
        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(new ReplyButton("text " + i, false, false));
        }
        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(buttons);
        assertAll(
                () -> assertEquals(numberOfButtons, actualKeyboard.getKeyboard().size()),
                () -> assertFalse(actualKeyboard.getOneTimeKeyboard()),
                () -> assertTrue(actualKeyboard.getResizeKeyboard())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2,10",
            "1,1",
            "5,2",
            "3,1"
    })
    void buildReplyShouldCreateKeyboardNotOneTimeAndResize(int maxNumberOfColumns, int numberOfButtons) {
        List<ReplyButton> buttons = new ArrayList<>(numberOfButtons);
        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(new ReplyButton("text " + i, false, false));
        }
        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(maxNumberOfColumns, buttons);
        assertAll(
                () -> assertFalse(actualKeyboard.getOneTimeKeyboard()),
                () -> assertTrue(actualKeyboard.getResizeKeyboard())
        );
    }

    @Test
    void buildReplyShouldCreateKeyboardWithResize() {
        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(2,
                List.of(ReplyButton.builder().text("some text").build()), true);
        assertAll(
                () -> assertTrue(actualKeyboard.getResizeKeyboard())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("buildReply(int maxNumberOfColumns, boolean oneTime, boolean resize, List<ReplyButton> buttons) " +
            "- передано невалидное значение количества колонок - проброшен TelegramCommonException")
    void buildReplyShouldThrowTelegramCommonExceptionIfNotValidMaxNumberOfColumns(int maxNumberOfColumns) {
        Executable executable = () -> keyboardBuilder.buildReply(
                maxNumberOfColumns, true, true,
                List.of(new ReplyButton("text", false, false))
        );
        assertThrows(TelegramCommonException.class, executable);
    }


    @Test
    @DisplayName("buildReply(int maxNumberOfColumns, boolean oneTime, boolean resize, List<ReplyButton> buttons) " +
            "- передано невалидное значение количества колонок - проброшен TelegramCommonException")
    void buildReplyShouldThrowTelegramCommonExceptionIfEmptyButtons() {
        Executable executable = () -> keyboardBuilder.buildReply(2, true, true, new ArrayList<>());
        assertThrows(TelegramCommonException.class, executable);
    }

    @Test
    @DisplayName("buildReply(int maxNumberOfColumns, boolean oneTime, boolean resize, List<ReplyButton> buttons) " +
            "- передано невалидное значение количества колонок - проброшен TelegramCommonException")
    void buildReplyShouldThrowTelegramCommonExceptionIfNullButtons() {
        Executable executable = () -> keyboardBuilder.buildReply(2, true, true, null);
        assertThrows(TelegramCommonException.class, executable);
    }

    @ParameterizedTest
    @CsvSource({
            "5,3,2",
            "1,2,1",
            "2,2,1",
            "3,2,2",
            "8,1,8"
    })
    void buildReplyShouldCreateKeyboardWithPassedParameterNumberOfColumns(int numberOfButtons, int maxNumberOfColumns, int expectedRowsNumber) {
        List<ReplyButton> buttons = new ArrayList<>();
        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(new ReplyButton("text " + i, false, false));
        }

        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(maxNumberOfColumns, false ,false, buttons);
        assertEquals(expectedRowsNumber, actualKeyboard.getKeyboard().size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void buildReplyShouldCreateWithPassedParameterOneTime(boolean oneTime) {
        List<ReplyButton> buttons = new ArrayList<>();
        buttons.add(new ReplyButton("text 1", false, false));
        buttons.add(new ReplyButton("text 2", false, false));

        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(2, oneTime, false , buttons);
        assertEquals(oneTime, actualKeyboard.getOneTimeKeyboard());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void buildReplyShouldCreateWithPassedParameterResize(boolean resize) {
        List<ReplyButton> buttons = new ArrayList<>();
        buttons.add(new ReplyButton("text 1", false, false));
        buttons.add(new ReplyButton("text 2", false, false));

        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(2, false, resize, buttons);
        assertEquals(resize, actualKeyboard.getResizeKeyboard());
    }

    @Test
    void buildReplyShouldCreateWithPassedRequestContact() {
        List<ReplyButton> buttons = new ArrayList<>();
        buttons.add(new ReplyButton("text 1", false, false));
        buttons.add(new ReplyButton("text 2", true, false));
        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(1, false, true, buttons);
        assertAll(
                () -> assertFalse(actualKeyboard.getKeyboard().getFirst().getFirst().getRequestContact()),
                () -> assertTrue(actualKeyboard.getKeyboard().get(1).getFirst().getRequestContact())
        );
    }

    @Test
    void buildReplyShouldCreateWithPassedRequestLocation() {
        List<ReplyButton> buttons = new ArrayList<>();
        buttons.add(new ReplyButton("text 1", false, true));
        buttons.add(new ReplyButton("text 2", true, false));
        ReplyKeyboardMarkup actualKeyboard = keyboardBuilder.buildReply(1, false, true, buttons);
        assertAll(
                () -> assertTrue(actualKeyboard.getKeyboard().getFirst().getFirst().getRequestLocation()),
                () -> assertFalse(actualKeyboard.getKeyboard().get(1).getFirst().getRequestLocation())
        );
    }
}