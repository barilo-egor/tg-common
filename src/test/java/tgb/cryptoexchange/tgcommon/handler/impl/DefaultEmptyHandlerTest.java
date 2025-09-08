package tgb.cryptoexchange.tgcommon.handler.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultEmptyHandlerTest {

    @ParameterizedTest
    @ValueSource(longs = {123456789L, 987654321L})
    @DisplayName("getEmptyMessage(Long chatId) - вызов - должен вернуть SendMessage с текстом \"Что-то пошло не так\"")
    void shouldReturnDefaultEmptyMessage(Long chatId) {
        DefaultEmptyHandler defaultEmptyHandler = new DefaultEmptyHandler();
        BotApiMethodMessage botApiMethodMessage = defaultEmptyHandler.getEmptyMessage(chatId);
        assertAll(
                () -> assertNotNull(botApiMethodMessage),
                () -> assertInstanceOf(SendMessage.class, botApiMethodMessage),
                () -> {
                    assert botApiMethodMessage instanceof SendMessage;
                    assertEquals(chatId, Long.parseLong(((SendMessage) botApiMethodMessage).getChatId()));
                },
                () -> {
                    assert botApiMethodMessage instanceof SendMessage;
                    assertEquals("Что-то пошло не так.", ((SendMessage) botApiMethodMessage).getText());
                }
        );
    }
}