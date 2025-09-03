package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Объект представление нажатой пользователем inline кнопки
 */
@Data
public class PressedInlineButton {

    /**
     * Идентификатор {@link CallbackQuery}
     */
    private String callbackQueryId;

    /**
     * data переданная через {@link CallbackQuery#getData()}
     */
    private String data;

    /**
     * Сообщение, к которому была прикреплена клавиатура с нажатой кнопкой
     */
    private MaybeInaccessibleMessage message;

    /**
     * Формирование кнопки из {@link CallbackQuery}
     * @param callbackQuery {@link CallbackQuery} из апдейта
     * @return сформированную кнопку
     */
    public static PressedInlineButton build(CallbackQuery callbackQuery) {
        var button = new PressedInlineButton();
        button.setData(callbackQuery.getData());
        button.setCallbackQueryId(callbackQuery.getId());
        button.setMessage(callbackQuery.getMessage());
        return button;
    }

    /**
     * Получение идентификатора чата пользователя нажавшего кнопку
     * @return чат айди
     */
    public Long getChatId() {
        return message.getChatId();
    }

    /**
     * Получение аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public String getArgument(int index) {
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        if (split.length - 1 < index) {
            return null;
        }
        return split[index];
    }

    /**
     * Получение long аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Long getLongArgument(int index) {
        String argument = getArgument(index);
        if (argument == null) {
            return null;
        }
        try {
            return Long.parseLong(argument);
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Long: data=%s, index=%s".formatted(data, index), e);
        }
    }

    /**
     * Получение int аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Integer getIntArgument(int index) {
        String argument = getArgument(index);
        if (argument == null) {
            return null;
        }
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Integer: data=" + data + ", index=" + index, e);
        }
    }

    /**
     * Получение всех аргументов в виде массива int
     * @return список значений
     */
    public Set<Integer> getIntArguments() {
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        Set<Integer> result = new HashSet<>();
        if (split.length <= 1) {
            return result;
        }
        for (int i = 1; i < split.length; i++) {
            result.add(Integer.parseInt(split[i]));
        }
        return result;
    }

    /**
     * Получение boolean аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Boolean getBoolArgument(int index) {
        String argument = getArgument(index);
        if (argument == null) {
            throw new NullPointerException("No boolean argument found");
        }
        if (Boolean.TRUE.toString().equalsIgnoreCase(argument)) {
            return true;
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(argument)) {
            return false;
        }
        throw new TelegramCommonException("Ошибка при парсинге к Boolean: data=" + data + ", index=" + index);
    }

    /**
     * Проверка наличия хотя бы одного аргумента
     * @return true если аргументы есть, false если нет
     */
    public boolean hasArguments() {
        if (Objects.isNull(data)) {
            return false;
        }
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        return split.length > 1;
    }
}
