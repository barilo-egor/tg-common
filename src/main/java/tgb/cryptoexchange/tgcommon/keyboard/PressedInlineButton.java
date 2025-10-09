package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Объект представление нажатой пользователем inline кнопки
 */
@Data
public class PressedInlineButton {
    
    private static final String INDEX = ", index=";

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
    public Optional<String> getArgument(int index) {
        if (data.isBlank()) {
            return Optional.empty();
        }
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        if (split.length - 1 < index) {
            return Optional.empty();
        }
        return Optional.of(split[index]);
    }

    /**
     * Получение long аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Optional<Long> getLongArgument(int index) {
        Optional<String> argument = getArgument(index);
        if (argument.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(argument.get()));
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Long: data=%s, index=%s".formatted(data, index), e);
        }
    }

    /**
     * Получение int аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Optional<Integer> getIntArgument(int index) {
        Optional<String> argument = getArgument(index);
        if (argument.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(argument.get()));
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Integer: data=" + data + INDEX + index, e);
        }
    }

    /**
     * Получение всех аргументов в виде массива int
     * @return список значений
     */
    public Set<Integer> getIntArguments() {
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        Set<Integer> result = new LinkedHashSet<>();
        if (split.length <= 1) {
            return result;
        }
        for (int i = 1; i < split.length; i++) {
            try {
                result.add(Integer.parseInt(split[i]));
            } catch (NumberFormatException e) {
                throw new TelegramCommonException("Ошибка при парсинге к Integer: data=" + data + INDEX + i, e);
            }
        }
        return result;
    }

    /**
     * Получение boolean аргумента из даты
     * @param index индекс (начиная с 1) аргумента
     * @return значение аргумента
     */
    public Optional<Boolean> getBoolArgument(int index) {
        Optional<String> argument = getArgument(index);
        if (argument.isEmpty()) {
            return Optional.empty();
        }
        String value = argument.get();
        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return Optional.of(true);
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return Optional.of(false);
        }
        throw new TelegramCommonException("Ошибка при парсинге к Boolean: data=" + data + INDEX + index);
    }

    /**
     * Проверка наличия хотя бы одного аргумента
     * @return true если аргументы есть, false если нет
     */
    public boolean hasArguments() {
        String[] split = data.split(InlineButton.CALLBACK_DATA_SPLITTER);
        return split.length > 1;
    }
}
