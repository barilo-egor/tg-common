package tgb.cryptoexchange.tgcommon.keyboard;

import lombok.Builder;
import lombok.Data;

/**
 * Представление Reply кнопки
 */
@Data
@Builder
public class ReplyButton {

    /**
     * Текст кнопки(также команда, если от бота требуется обработка по нажатию)
     */
    private String text;

    /**
     * Будет ли отправлен контакт пользователем по нажатию на кнопку
     */
    private boolean isRequestContact;

    /**
     * Будет ли отправлена геолокация по нажатию на кнопку
     */
    private boolean isRequestLocation;

}