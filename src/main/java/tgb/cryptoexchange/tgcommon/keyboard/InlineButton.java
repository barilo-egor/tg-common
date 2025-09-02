package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InlineButton {
    private String text;
    private String data;
    private InlineType inlineType;

    public static InlineButton buildData(String text, String data) {
        return build(InlineType.CALLBACK_DATA, text, data);
    }

    public static InlineButton build(InlineType inlineType, String text, String data) {
        return InlineButton.builder()
                .text(text)
                .data(data)
                .inlineType(inlineType)
                .build();
    }

    public enum InlineType {
        CALLBACK_DATA,
        URL,
        SWITCH_INLINE_QUERY,
        SWITCH_INLINE_QUERY_CURRENT_CHAT,
        WEB_APP
    }
}
