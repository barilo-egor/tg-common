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
}
