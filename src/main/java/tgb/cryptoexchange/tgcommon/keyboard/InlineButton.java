package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.CallbackQueryData;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static tgb.cryptoexchange.tgcommon.service.CallbackDataService.SPLITTER;

/**
 * Представление Inline кнопки
 */
@Data
@Builder
public class InlineButton {
    private String text;
    private CallbackQueryData callbackQueryData;
    private InlineType inlineType;

    /**
     * Тип инлайн кнопки
     */
    public enum InlineType {
        CALLBACK_DATA,
        URL,
        SWITCH_INLINE_QUERY,
        SWITCH_INLINE_QUERY_CURRENT_CHAT,
        WEB_APP
    }

}
