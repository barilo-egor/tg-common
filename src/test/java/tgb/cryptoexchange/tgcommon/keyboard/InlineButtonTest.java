package tgb.cryptoexchange.tgcommon.keyboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InlineButtonTest {

    @ParameterizedTest
    @CsvSource({
            "Some text, CALLBACK_DATA, callbackQueryHandlerId",
            "5, CALLBACK_DATA, callbackQueryHandlerId:123:str",
            "Google, URL, https://google.com",
            "Share something, SWITCH_INLINE_QUERY, Share from bot"
    })
    void shouldCreateInlineTypeButton(String text, InlineButton.InlineType inlineType, String data) {
        InlineButton button = new InlineButton(text, inlineType, data);
        assertEquals(text, button.getText());
        assertEquals(inlineType, button.getInlineType());
        assertEquals(data, button.getData());
    }

    @ParameterizedTest
    @CsvSource({
            "some id, some text",
            "1,2",
            "id123,text543"
    })
    void shouldCreateCallbackDataButtonWithoutArguments(String id, String text) {
        InlineButton button = new InlineButton(id, text);
        assertEquals(text, button.getText());
        assertEquals(InlineButton.InlineType.CALLBACK_DATA, button.getInlineType());
        assertEquals(id, button.getData());
    }

    @ParameterizedTest
    @CsvSource({
            "some id, some text",
            "1,2",
            "id123,text543"
    })
    void shouldCreateCallbackDataButtonWithNullListArguments(String id, String text) {
        InlineButton button = new InlineButton(id, text, (List<Object>) null);
        assertEquals(text, button.getText());
        assertEquals(InlineButton.InlineType.CALLBACK_DATA, button.getInlineType());
        assertEquals(id, button.getData());
    }

    @ParameterizedTest
    @ValueSource( strings = {
            "arg1,arg2,arg3",
            "1,2,3,55000",
            "1",
            "a",
            "5.6,qwerty,true"
    })
    void shouldCreateCallbackDataButtonWithVarArgsArguments(String argsString) {
        Object[] args = argsString.split(",");
        InlineButton inlineButton = new InlineButton("someid", "Text", args);
        String expected = "someid:" + argsString.replace(",",":");
        assertEquals(expected, inlineButton.getData());
    }

    @ParameterizedTest
    @ValueSource( strings = {
            "arg1,arg2,arg3",
            "1,2,3,55000",
            "1",
            "a",
            "5.6,qwerty,true"
    })
    void shouldCreateCallbackDataButtonWithListArguments(String argsString) {
        Object[] args = argsString.split(",");
        InlineButton inlineButton = new InlineButton("someid", "Text", Arrays.asList(args));
        String expected = "someid:" + argsString.replace(",",":");
        assertEquals(expected, inlineButton.getData());
    }

    @Test
    void shouldThrowNullPointerIfNullTextInInlineTypeConstructor() {
        assertThrows(NullPointerException.class, () -> new InlineButton(null, InlineButton.InlineType.CALLBACK_DATA, "data"));
    }

    @Test
    void shouldThrowNullPointerIfNullDataInInlineTypeConstructor() {
        assertThrows(NullPointerException.class, () -> new InlineButton("text", InlineButton.InlineType.CALLBACK_DATA, null));
    }

    @Test
    void shouldThrowNullPointerIfNullIdInCallbackDataConstructorWithVarArgs() {
        assertThrows(NullPointerException.class, () -> new InlineButton(null, "text"));
    }

    @Test
    void shouldThrowNullPointerIfNullTextInCallbackDataConstructorWithVarArgs() {
        assertThrows(NullPointerException.class, () -> new InlineButton("id", null));
    }

    @Test
    void shouldThrowNullPointerIfNullArgumentInCallbackDataConstructorWithVarArgs() {
        assertThrows(NullPointerException.class, () -> new InlineButton("id", "text", "arg1", null));
    }

    @Test
    void shouldThrowNullPointerIfNullIdInCallbackDataConstructorWithList() {
        Executable constructor = () -> new InlineButton(null, "text", new ArrayList<>());
        assertThrows(NullPointerException.class, constructor);
    }

    @Test
    void shouldThrowNullPointerIfNullTextInCallbackDataConstructorWithList() {
        Executable constructor = () -> new InlineButton("id", null, new ArrayList<>());
        assertThrows(NullPointerException.class, constructor);
    }
}