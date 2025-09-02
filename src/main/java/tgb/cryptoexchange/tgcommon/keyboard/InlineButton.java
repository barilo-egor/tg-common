package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Builder;
import lombok.Data;
import tgb.cryptoexchange.tgcommon.constants.CallbackQueryData;
import tgb.cryptoexchange.tgcommon.handler.CallbackQueryHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Представление Inline кнопки
 */
@Data
@Builder
public class InlineButton {

    public static final String CALLBACK_DATA_SPLITTER = ":";

    /**
     * Текст кнопки
     */
    private String text;

    /**
     * Уникальная строка обработчика ({@link CallbackQueryHandler#getId()} ()}).
     * По ней будет определен нужный обработчик при нажатии на кнопку пользователем.
     */
    private String id;

    /**
     * Тип кнопки
     */
    private InlineType inlineType;

    /**
     * Аргументы кнопки (для {@link InlineType#CALLBACK_DATA})
     */
    private List<String> arguments;

    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public void addArguments(Collection<String> arguments) {
        this.arguments.addAll(arguments);
    }

    public void addArguments(String... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
    }

    /**
     * Формирование data для кнопки. В случае, если тип кнопки {@link InlineType#CALLBACK_DATA}, то к data
     * добавляются аргументы. Иначе в data будет только {@link CallbackQueryData#getData()}
     *
     * @return сформированная data
     */
    public String buildData() {
        return id + (
                InlineType.CALLBACK_DATA.equals(inlineType)
                        ? CALLBACK_DATA_SPLITTER + String.join(":", arguments)
                        : ""
        );
    }

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
