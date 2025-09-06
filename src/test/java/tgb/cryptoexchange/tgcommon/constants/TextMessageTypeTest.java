package tgb.cryptoexchange.tgcommon.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextMessageTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"/start", "/help"})
    @DisplayName("fromString(String text) - текст начинается со слеша - возвращен SLASH_COMMAND")
    void shouldReturnSLASH_COMMANDIfStartsFromSlash(String command) {
        assertEquals(TextMessageType.SLASH_COMMAND, TextMessageType.fromString(command));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Настройки", "Вернуться"})
    @DisplayName("fromString(String text) - текст не начинается со слеша - возвращен TEXT_COMMAND")
    void shouldReturnTEXT_COMMANDIfNotStartsFromSlash(String command) {
        assertEquals(TextMessageType.TEXT_COMMAND, TextMessageType.fromString(command));
    }
}