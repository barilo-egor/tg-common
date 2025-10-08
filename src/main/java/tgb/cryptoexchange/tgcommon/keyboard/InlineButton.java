package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Getter;
import lombok.NonNull;
import tgb.cryptoexchange.tgcommon.constants.CallbackQueryData;
import tgb.cryptoexchange.tgcommon.handler.CallbackQueryHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Представление Inline кнопки
 */
@Getter
public class InlineButton {

    public static final String CALLBACK_DATA_SPLITTER = ":";

    /**
     * Текст кнопки
     */
    private final String text;

    /**
     * Тип кнопки
     */
    private final InlineType inlineType;

    /**
     * Данные, которые будут отправлены при {@link InlineType#CALLBACK_DATA}.
     * Url при {@link InlineType#URL}.
     * Текст, который будет вставлен в чат с другим пользователем при нажатии на кнопку при {@link InlineType#SWITCH_INLINE_QUERY}.
     * Текст, который будет вставлен в чат при нажатии на кнопку при {@link InlineType#SWITCH_INLINE_QUERY_CURRENT_CHAT}.
     * Url при {@link InlineType#WEB_APP}.
     */
    private final String data;

    /**
     * Формирование инлайн кнопки.
     *
     * @param text       текст кнопки
     * @param inlineType тип кнопки
     * @param data       данные кнопки: <br>
     *                   данные, которые будут отправлены при {@link InlineType#CALLBACK_DATA},
     *                   url при {@link InlineType#URL}
     *                   текст, который будет вставлен в чат с другим пользователем при нажатии на кнопку при {@link InlineType#SWITCH_INLINE_QUERY},
     *                   текст, который будет вставлен в чат при нажатии на кнопку при {@link InlineType#SWITCH_INLINE_QUERY_CURRENT_CHAT},
     *                   url при {@link InlineType#WEB_APP}
     */
    public InlineButton(@NonNull String text, InlineType inlineType, @NonNull String data) {
        this.text = text;
        this.inlineType = inlineType;
        this.data = data;
    }

    /**
     * Формирование инлайн кнопки с типом {@link InlineType#CALLBACK_DATA}. Формирование данных происходит путем
     * склеивания идентификатора {@link CallbackQueryHandler} и переданных в параметре аргументов
     * используя {@link InlineButton#CALLBACK_DATA_SPLITTER}.
     *
     * @param id        идентификатор {@link CallbackQueryHandler}
     * @param text      текст кнопки
     * @param arguments аргументы, которые будут переданы боту при нажатии на кнопку
     */
    public InlineButton(@NonNull String id, @NonNull String text, Object... arguments) {
        this.text = text;
        this.inlineType = InlineType.CALLBACK_DATA;
        if (Stream.of(arguments).anyMatch(Objects::isNull)) {
            throw new NullPointerException("some argument is null");
        }
        this.data = buildData(id, Arrays.asList(arguments));
    }

    /**
     * Формирование инлайн кнопки с типом {@link InlineType#CALLBACK_DATA}. Формирование данных происходит путем
     * склеивания идентификатора {@link CallbackQueryHandler} и переданных в параметре аргументов
     * используя {@link InlineButton#CALLBACK_DATA_SPLITTER}.
     *
     * @param id        идентификатор {@link CallbackQueryHandler}
     * @param text      текст кнопки
     * @param arguments аргументы, которые будут переданы боту при нажатии на кнопку
     */
    public InlineButton(@NonNull String id, @NonNull String text, List<Object> arguments) {
        this.text = text;
        this.inlineType = InlineType.CALLBACK_DATA;
        if (Objects.isNull(arguments)) {
            arguments = new ArrayList<>();
        }
        this.data = buildData(id, arguments);
    }

    /**
     * Формирование data для кнопки. В случае, если тип кнопки {@link InlineType#CALLBACK_DATA}, то к data
     * добавляются аргументы. Иначе в data будет только {@link CallbackQueryData#getData()}
     *
     * @return сформированная data
     */
    private String buildData(String id, List<Object> arguments) {
        return id + (
                !arguments.isEmpty()
                        ? CALLBACK_DATA_SPLITTER + String.join(":", arguments.stream().map(Object::toString).toList())
                        : ""
        );
    }

    /**
     * Тип инлайн кнопки
     */
    public enum InlineType {
        /**
         * Кнопка с данными передаваемыми боту при нажатии пользователем кнопки
         */
        CALLBACK_DATA,
        /**
         * Кнопка с URL по которому будет осуществлен переход при нажатии на кнопку
         */
        URL,
        /**
         * Кнопка, по которой пользователю будет предложен выбор чата куда будет вставлена ссылка бота и переданные данные.
         */
        SWITCH_INLINE_QUERY,
        /**
         * Кнопка, по нажатию на которую в чате с ботом пользователю будет вставлена ссылка бота и переданные данные.
         */
        SWITCH_INLINE_QUERY_CURRENT_CHAT,
        /**
         * Кнопка с URL, страница которого будет открыта в приложении телеграм.
         */
        WEB_APP
    }

}
