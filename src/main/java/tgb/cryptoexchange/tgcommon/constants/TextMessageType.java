package tgb.cryptoexchange.tgcommon.constants;

public enum TextMessageType {
    SLASH_COMMAND,
    TEXT_COMMAND;

    public static TextMessageType fromString(String text) {
        if (text.startsWith("/")) {
            return SLASH_COMMAND;
        }
        return TEXT_COMMAND;
    }
}
