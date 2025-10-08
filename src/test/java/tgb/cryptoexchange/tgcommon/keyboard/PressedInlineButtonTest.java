package tgb.cryptoexchange.tgcommon.keyboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PressedInlineButtonTest {

    @ParameterizedTest
    @CsvSource({
            "some:data:1,id1,5325253"
    })
    @DisplayName("build(CallbackQuery callbackQuery) - вызов с произвольным callbackQuery " +
            "- создана соответствующая кнопка исходя из данных callbackQuery")
    void buildShouldReturnMatchButton(String data, String id, Long chatId) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(id);
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        PressedInlineButton actual = PressedInlineButton.build(callbackQuery);
        assertAll(
                () -> assertEquals(id, actual.getCallbackQueryId()),
                () -> assertEquals(data, actual.getData()),
                () -> assertEquals(message, actual.getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {123455678L, 987654321L, 123543L})
    @DisplayName("getChatId() - должен вернуть идентификатор чата из message")
    void getChatIdShouldReturnChatId(Long chatId) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("some data");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertEquals(chatId, actual.getChatId());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 10, 2445})
    @DisplayName("getArgument(int index) - передан индекс >= количеству аргументов - пустой Optional")
    void getArgumentShouldReturnNullIfIndexOutOfBounds(int index) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("someid:12345:qwe:2.45");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.getArgument(index).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "id1:1,1,1",
            "id2:data:5.65,2,5.65",
            "id3:qwerty:24:asdfgh,1,qwerty",
            "id4:zxc:123,0,id4",
            "id5:qwe:24.24:213.54:123,4,123"
    })
    @DisplayName("getArgument(int index) - передан индекс < количеству аргументов - возвращен аргумент")
    void getArgumentShouldReturnArgumentIfIndexInBounds(String data, int index, String excepted) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Optional<String> maybeArgument = actual.getArgument(index);
        assertTrue(maybeArgument.isPresent());
        assertEquals(excepted, maybeArgument.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 10, 2445})
    @DisplayName("getLongArgument(int index) - передан индекс >= количеству аргументов - пустой Optional")
    void getLongArgumentShouldReturnNullIfIndexOutOfBounds(int index) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("someid:12345:qwe:2.45");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.getLongArgument(index).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "id1:-1,1,-1",
            "id2:data:0,2,0",
            "id3:qwerty:123123123123123:asdfgh,2,123123123123123",
    })
    @DisplayName("getLongArgument(int index) - передан индекс < количеству аргументов - возвращен аргумент типа long")
    void getLongArgumentShouldReturnArgumentIfIndexInBounds(String data, int index, Long excepted) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Optional<Long> maybeArgument = actual.getLongArgument(index);
        assertTrue(maybeArgument.isPresent());
        assertEquals(excepted, maybeArgument.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "someData:notNumber",
            "againData:notNumberToo12353523",
            "doubleData:1535.235235"
    })
    @DisplayName("getLongArgument(int index) - попытка получить аргумент типа long из невалидного значения - проброшен TelegramCommonException")
    void getLongArgumentShouldThrowTelegramCommonExceptionIfNumberFormatExceptionWasThrown(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Executable executable = () -> actual.getLongArgument(1);
        assertThrows(TelegramCommonException.class, executable);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 10, 2445})
    @DisplayName("getIntArgument(int index) - передан индекс >= количеству аргументов - возвращен пустой Optional")
    void getIntArgumentShouldReturnNullIfIndexOutOfBounds(int index) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("someid:12345:qwe:2.45");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.getIntArgument(index).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "id1:-1,1,-1",
            "id2:data:0,2,0",
            "id3:qwerty:123123123:asdfgh,2,123123123",
    })
    @DisplayName("getIntArgument(int index) - передан индекс < количеству аргументов - возвращен аргумент типа int")
    void getIntArgumentShouldReturnArgumentIfIndexInBounds(String data, int index, Integer excepted) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Optional<Integer> maybeArgument = actual.getIntArgument(index);
        assertTrue(maybeArgument.isPresent());
        assertEquals(excepted, maybeArgument.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "someData:notNumber",
            "againData:notNumberToo12353523",
            "doubleData:1535.235235"
    })
    @DisplayName("getIntArgument(int index) - попытка получить аргумент типа long из невалидного значения - проброшен TelegramCommonException")
    void getIntArgumentShouldThrowTelegramCommonExceptionIfNumberFormatExceptionWasThrown(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Executable executable = () -> actual.getIntArgument(1);
        assertThrows(TelegramCommonException.class, executable);
    }

    @Test
    @DisplayName("getIntArguments() - data без аргументов - возвращен пустой сет")
    void getIntArgumentsShouldReturnEmptySet() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("data");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.getIntArguments().isEmpty());
    }

    @Test
    @DisplayName("getIntArguments() - data c одним числовым аргументом - возвращен сет с одним аргументом")
    void getIntArgumentsShouldReturnWithOneArgument() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("data:123");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery).getIntArguments();
        assertEquals(1, actual.size());
        assertEquals(123, actual.iterator().next());
    }

    @Test
    @DisplayName("getIntArguments() - data c тремя числовыми аргументами - возвращен сет с тремя аргументами")
    void getIntArgumentsShouldReturnWithThreeArguments() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("data:123:456:789");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery).getIntArguments();
        var iterator = actual.iterator();
        assertEquals(3, actual.size());
        assertEquals(123, iterator.next());
        assertEquals(456, iterator.next());
        assertEquals(789, iterator.next());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "data:qwe",
            "data:123:qwe",
            "data:qwe:123",
            "data:2.54,245,123"
    })
    @DisplayName("getIntArguments() - одно из значений не число - проброшен TelegramCommonException")
    void getIntArgumentsShouldThrowTelegramCommonExceptionIfDataHasNotNumber(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        assertThrows(TelegramCommonException.class, () -> PressedInlineButton.build(callbackQuery).getIntArguments());
    }


    @ParameterizedTest
    @ValueSource(ints = {4, 10, 2445})
    @DisplayName("getBoolArgument(int index) - передан индекс >= количеству аргументов - пустой Optional")
    void getBoolArgumentShouldReturnNullIfIndexOutOfBounds(int index) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData("someid:12345:qwe:2.45");
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.getBoolArgument(index).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "id1:true,1,true",
            "id2:data:false,2,false",
            "id3:qwerty:true:asdfgh,2,true",
    })
    @DisplayName("getIntArgument(int index) - передан индекс < количеству аргументов - возвращен аргумент типа int")
    void getBoolArgumentShouldReturnArgumentIfIndexInBounds(String data, int index, boolean excepted) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Optional<Boolean> maybeArgument = actual.getBoolArgument(index);
        assertTrue(maybeArgument.isPresent());
        assertEquals(excepted, maybeArgument.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "someData:notBoolean",
            "againData:12353523",
            "doubleData:1535.235235"
    })
    @DisplayName("getIntArgument(int index) - попытка получить аргумент типа long из невалидного значения - проброшен TelegramCommonException")
    void getBoolArgumentShouldThrowTelegramCommonExceptionIfNumberFormatExceptionWasThrown(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        Executable executable = () -> actual.getBoolArgument(1);
        assertThrows(TelegramCommonException.class, executable);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "somedata1",
            "123",
            "data"
    })
    @DisplayName(("hasArguments() - в data нет аргументов - возвращен false"))
    void hasArgumentsShouldReturnFalseIfNoArguments(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertFalse(actual.hasArguments());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "data:1",
            "data:arg",
            "data:arg:2.53",
            "data:5:123123543:123"
    })
    @DisplayName("hasArguments() - в data присутствуют аргументы - возвращен true")
    void hasArgumentsShouldReturnTrueIfDataHasArguments(String data) {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123456789L);
        message.setChat(chat);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("some id");
        callbackQuery.setData(data);
        callbackQuery.setMessage(message);

        var actual = PressedInlineButton.build(callbackQuery);
        assertTrue(actual.hasArguments());
    }
}