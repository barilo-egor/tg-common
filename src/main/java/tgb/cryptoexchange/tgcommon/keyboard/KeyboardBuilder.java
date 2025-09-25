package tgb.cryptoexchange.tgcommon.keyboard;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tgb.cryptoexchange.tgcommon.keyboard.InlineButton.InlineType.CALLBACK_DATA;

/**
 * Сервисный класс для формирования клавиатур
 */
@Service
public class KeyboardBuilder {

    /**
     * Формирование inline клавиатуры исходя из списка кнопок по одной в каждом ряду
     * @param buttons inline кнопки
     * @return готовую для отправки клавиатуру
     */
    public InlineKeyboardMarkup buildInline(List<InlineButton> buttons) {
        return buildInline(1, buttons);
    }

    /**
     * Формирование inline клавиатуры исходя из списка кнопок и количества кнопок в ряду
     * @param maxNumberOfColumns максимальное количество кнопок в ряду
     * @param buttons inline кнопки
     * @return готовую для отправки клавиатуру
     */
    public InlineKeyboardMarkup buildInline(int maxNumberOfColumns, List<InlineButton> buttons) {
        if (maxNumberOfColumns < 1) {
            throw new TelegramCommonException("Количество колонок не может быть меньше одного.");
        }
        if (Objects.isNull(buttons) || buttons.isEmpty()) {
            throw new TelegramCommonException("Должна присутствовать хотя бы одна кнопка");
        }
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < buttons.size(); i++) {
            row.add(parse(buttons.get(i)));
            j++;
            if (j == maxNumberOfColumns || i == (buttons.size() - 1)) {
                rows.add(row);
                row = new ArrayList<>();
                j = 0;
            }
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    /**
     * Создание клавиатуры из рядов кнопок
     * @param rows список рядов с кнопками
     * @return готовую для отправки клавиатуру
     */
    public InlineKeyboardMarkup buildInlineByRows(List<List<InlineButton>> rows) {
        List<List<InlineKeyboardButton>> mapped = new ArrayList<>();
        for (List<InlineButton> buttons : rows) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (InlineButton button: buttons) {
                row.add(parse(button));
            }
            mapped.add(row);
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(mapped)
                .build();
    }

    private InlineKeyboardButton parse(InlineButton inlineButton) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(inlineButton.getText());
        String data = inlineButton.getData();
        InlineButton.InlineType inlineType = inlineButton.getInlineType();
        switch (Objects.isNull(inlineType) ? CALLBACK_DATA : inlineType) {
            case URL:
                inlineKeyboardButton.setUrl(data);
                break;
            case CALLBACK_DATA:
                inlineKeyboardButton.setCallbackData(data);
                break;
            case SWITCH_INLINE_QUERY:
                inlineKeyboardButton.setSwitchInlineQuery(data);
                break;
            case SWITCH_INLINE_QUERY_CURRENT_CHAT:
                inlineKeyboardButton.setSwitchInlineQueryCurrentChat(data);
                break;
            case WEB_APP:
                inlineKeyboardButton.setWebApp(WebAppInfo.builder().url(data).build());
                break;
        }
        return inlineKeyboardButton;
    }

    /**
     * Формирование reply клавиатуры из одной колонки, без скрытия после нажатия и ресайзом клавиатура
     * @param buttons список кнопок клавиатуры
     * @return готовую для отправки клавиатуру
     */
    public ReplyKeyboardMarkup buildReply(List<ReplyButton> buttons) {
        return buildReply(1, false, true, buttons);
    }

    /**
     * Формирование reply клавиатуры без скрытия после нажатия и ресайзом клавиатура
     * @param maxNumberOfColumns максимальное количество кнопок в одном ряду
     * @param buttons список кнопок клавиатуры
     * @return готовую для отправки клавиатуру
     */
    public ReplyKeyboardMarkup buildReply(int maxNumberOfColumns, List<ReplyButton> buttons) {
        return buildReply(maxNumberOfColumns, false, true, buttons);
    }

    /**
     * Формирование reply клавиатуры с ресайзом клавиатуры
     * @param maxNumberOfColumns максимальное количество кнопок в одном ряду
     * @param oneTime будет ли клавиатура скрыта после нажатия кнопки
     * @param buttons список кнопок клавиатуры
     * @return готовую для отправки клавиатуру
     */
    public ReplyKeyboardMarkup buildReply(int maxNumberOfColumns, List<ReplyButton> buttons, boolean oneTime) {
        return buildReply(maxNumberOfColumns, oneTime, true, buttons);
    }

    /**
     * Формирование reply клавиатуры
     * @param maxNumberOfColumns максимальное количество кнопок в одном ряду
     * @param oneTime будет ли клавиатура скрыта после нажатия кнопки
     * @param resize true чтобы подстроить вертикальную высоту клавиатуры, false чтобы оставить высоту по умолчанию
     * @param buttons список кнопок клавиатуры
     * @return готовую для отправки клавиатуру
     */
    public ReplyKeyboardMarkup buildReply(int maxNumberOfColumns, boolean oneTime, boolean resize, List<ReplyButton> buttons) {
        if (maxNumberOfColumns < 1)
            throw new TelegramCommonException("Количество колонок не может быть меньше 1.");
        if (Objects.isNull(buttons) || buttons.isEmpty())
            throw new TelegramCommonException("Должна присутствовать хотя бы одна кнопка.");
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        int j = 0;
        for (int i = 0; i < buttons.size(); i++) {
            KeyboardButton keyboardButton = KeyboardButton.builder()
                    .text(buttons.get(i).getText())
                    .build();
            keyboardButton.setRequestContact(buttons.get(i).isRequestContact());
            keyboardButton.setRequestLocation(buttons.get(i).isRequestLocation());
            row.add(keyboardButton);
            j++;
            if (j == maxNumberOfColumns || i == (buttons.size() - 1)) {
                rows.add(row);
                row = new KeyboardRow();
                j = 0;
            }
        }
        return ReplyKeyboardMarkup.builder()
                .oneTimeKeyboard(oneTime)
                .resizeKeyboard(resize)
                .keyboard(rows)
                .build();
    }
}
