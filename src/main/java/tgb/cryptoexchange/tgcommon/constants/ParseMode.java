package tgb.cryptoexchange.tgcommon.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ParseMode {
    NONE(""),
    HTML("html"),
    MARKDOWN("markdown"),;

    private final String value;

    public static ParseMode fromValue(String value) {
        for (ParseMode mode : ParseMode.values()) {
            if (mode.getValue().equals(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
