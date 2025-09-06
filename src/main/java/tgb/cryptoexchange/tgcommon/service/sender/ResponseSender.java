package tgb.cryptoexchange.tgcommon.service.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgb.cryptoexchange.tgcommon.bot.BotInstance;
import tgb.cryptoexchange.tgcommon.keyboard.InlineButton;
import tgb.cryptoexchange.tgcommon.keyboard.KeyboardBuilder;
import tgb.cryptoexchange.tgcommon.keyboard.ReplyButton;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Класс для отправки ответов пользователю.
 */
@Component
@Slf4j
public class ResponseSender {

    private final BotInstance bot;
    private final KeyboardBuilder keyboardBuildService;

    public ResponseSender(BotInstance bot, KeyboardBuilder keyboardBuildService) {
        this.bot = bot;
        this.keyboardBuildService = keyboardBuildService;
    }

    /**
     * Отправка текстового сообщения пользователю без клавиатуры и ответа на другое сообщение.
     *
     * @param chatId чат айди пользователя
     * @param text   текст сообщения
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text) {
        return sendMessage(chatId, text, null, null);
    }

    /**
     * Отправка текстового сообщения пользователю без клавиатуры
     *
     * @param chatId           чат айди пользователя
     * @param text             текст сообщения
     * @param replyToMessageId идентификатор сообщения на которое будет сделан ответ
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, Integer replyToMessageId) {
        return sendMessage(chatId, text, null, replyToMessageId);
    }

    /**
     * Отправка текстового сообщения пользователю без ответа на другое сообщение
     *
     * @param chatId        чат айди пользователя
     * @param text          текст сообщения
     * @param replyKeyboard клавиатура
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        return sendMessage(chatId, text, replyKeyboard, null);
    }

    /**
     * Отправка текстового сообщения пользователю без ответа на другое сообщение
     *
     * @param chatId       чат айди пользователя
     * @param text         текст сообщения
     * @param replyButtons кнопки reply клавиатуры
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, ReplyButton... replyButtons) {
        return sendMessage(chatId, text, keyboardBuildService.buildReply(List.of(replyButtons)), null);
    }

    /**
     * Отправка текстового сообщения пользователю без ответа на другое сообщение
     *
     * @param chatId        чат айди пользователя
     * @param text          текст сообщения
     * @param inlineButtons кнопки inline клавиатуры
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, InlineButton... inlineButtons) {
        return sendMessage(chatId, text, keyboardBuildService.buildInline(List.of(inlineButtons)), null);
    }

    /**
     * Отправка текстового сообщения пользователю без ответа на другое сообщение
     *
     * @param chatId        чат айди пользователя
     * @param text          текст сообщения
     * @param inlineButtons кнопки inline клавиатуры
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, List<InlineButton> inlineButtons) {
        return sendMessage(chatId, text, keyboardBuildService.buildInline(inlineButtons), null);
    }

    /**
     * Отправка текстового сообщения пользователю
     *
     * @param chatId           чат айди пользователя
     * @param text             текст сообщения
     * @param replyKeyboard    клавиатура
     * @param replyToMessageId идентификатор сообщения на которое будет сделан ответ
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, Integer replyToMessageId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyToMessageId(replyToMessageId)
                .replyMarkup(replyKeyboard)
                .parseMode("html")
                .build();
        return Optional.ofNullable(executeSendMessage(sendMessage));
    }

    private Message executeSendMessage(SendMessage sendMessage) {
        try {
            sendMessage.setParseMode("html");
            return bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки sendMessage: ", e);
            return null;
        }
    }

    /**
     * Отправка текстового сообщения пользователю без клавиатуры и ответа на другое сообщение без обработки исключения
     * в случае неудочной отправки сообщения
     *
     * @param chatId чат айди пользователя
     * @param text   текст сообщения
     */
    public void sendMessageThrows(Long chatId, String text) throws TelegramApiException {
        bot.execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("html")
                .build());
    }

    /**
     * Отправка сообщения с изображением пользователю без клавиатуры
     *
     * @param chatId  чат айди пользователя
     * @param caption текст подписи
     * @param photo   отправляемое изображение
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendPhoto(Long chatId, String caption, InputFile photo) {
        return sendPhoto(chatId, caption, photo, null);
    }

    /**
     * Отправка сообщения с изображением пользователю
     *
     * @param chatId        чат айди пользователя
     * @param caption       текст подписи
     * @param photo         telegram file id изображения
     * @param replyKeyboard клавиатура
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendPhoto(Long chatId, String caption, String photo, ReplyKeyboard replyKeyboard) {
        return sendPhoto(chatId, caption, new InputFile(photo), replyKeyboard);
    }

    /**
     * Отправка сообщения с изображением пользователю без клавиатуры
     *
     * @param chatId  чат айди пользователя
     * @param caption текст подписи
     * @param photo   отправляемое изображение
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendPhoto(Long chatId, String caption, String photo) {
        return sendPhoto(chatId, caption, new InputFile(photo), null);
    }

    /**
     * Отправка сообщения с изображением пользователю
     *
     * @param chatId        чат айди пользователя
     * @param caption       текст подписи
     * @param photo         отправляемое изображение
     * @param replyKeyboard клавиатура
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendPhoto(Long chatId, String caption, InputFile photo, ReplyKeyboard replyKeyboard) {
        try {
            return Optional.ofNullable(bot.execute(SendPhoto.builder()
                    .chatId(chatId.toString())
                    .caption(caption)
                    .photo(photo)
                    .replyMarkup(replyKeyboard)
                    .parseMode("html")
                    .build()));
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendPhoto: ", e);
            return Optional.empty();
        }
    }

    /**
     * Отправка сообщения с анимацией пользователю без клавиатуры и подписи
     *
     * @param chatId чат айди пользователя
     * @param file   отправляемая анимация
     * @return сообщение, если оно было успешно доставлено
     */
    public Message sendAnimation(Long chatId, File file) {
        return sendAnimation(chatId, new InputFile(file), null, null);
    }

    /**
     * Отправка сообщения с анимацией пользователю
     *
     * @param chatId        чат айди пользователя
     * @param caption       текст подписи
     * @param animation     telegram file id анимации
     * @param replyKeyboard клавиатура
     * @return сообщение, если оно было успешно доставлено
     */
    public Optional<Message> sendAnimation(Long chatId, String caption, String animation, ReplyKeyboard replyKeyboard) {
        return Optional.ofNullable(sendAnimation(chatId, new InputFile(animation), caption, replyKeyboard));
    }

    /**
     * Отправка сообщения с анимацией пользователю
     *
     * @param chatId        чат айди пользователя
     * @param caption       текст подписи
     * @param animation     отправляемая анимация
     * @param replyKeyboard клавиатура
     * @return сообщение, если оно было успешно доставлено
     */
    public Message sendAnimation(Long chatId, InputFile animation, String caption, ReplyKeyboard replyKeyboard) {
        try {
            return bot.execute(SendAnimation.builder()
                    .chatId(chatId.toString())
                    .animation(animation)
                    .caption(caption)
                    .replyMarkup(replyKeyboard)
                    .parseMode("html")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendAnimation: ", e);
            return null;
        }
    }

    /**
     * Удаление сообщения из чата с пользователем
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор удаляемого сообщения
     */
    public void deleteMessage(Long chatId, Integer messageId) {
        try {
            bot.execute(DeleteMessage.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки DeleteMessage: ", e);
        }
    }

    /**
     * Изменение сообщения в чате с пользователем без клавиатуры
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     */
    public void sendEditedMessageText(Long chatId, Integer messageId, String text) {
        sendEditedMessageText(chatId, messageId, text, (InlineKeyboardMarkup) null);
    }

    /**
     * Изменение сообщения в чате с пользователем
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param buttons   inline кнопки клавиатуры
     */
    public void sendEditedMessageText(Long chatId, Integer messageId, String text, List<InlineButton> buttons) {
        sendEditedMessageText(chatId, messageId, text, keyboardBuildService.buildInline(buttons));
    }

    /**
     * Изменение сообщения в чате с пользователем
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param keyboard  inline клавиатура
     */
    public void sendEditedMessageText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        try {
            bot.execute(EditMessageText.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .text(text)
                    .replyMarkup(keyboard)
                    .parseMode("html")
                    .build());
        } catch (TelegramApiException e) {
            log.debug("Ошибка отправки EditMessageText: ", e);
        }
    }

    /**
     * Изменение сообщения в чате с пользователем без обработки исключения в случае неудачного изменения сообщения
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param keyboard  inline клавиатура
     */
    public void sendEditedMessageTextThrows(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) throws TelegramApiException {
        bot.execute(EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .parseMode("html")
                .build());
    }

    /**
     * Изменение медиа группы в чате с пользователем
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param keyboard  inline клавиатура
     */
    public void sendEditMessageMedia(Long chatId, Integer messageId, String fileId, String text, InlineKeyboardMarkup keyboard) {
        try {
            bot.execute(build(chatId, messageId, fileId, text, keyboard));
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки EditMessageMedia: ", e);
        }
    }

    /**
     * Изменение группы медиа в чате с пользователем без обработки исключения в случае неудачного изменения сообщения
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param keyboard  inline клавиатура
     */
    public void sendEditMessageMediaThrows(Long chatId, Integer messageId, String fileId, String text, InlineKeyboardMarkup keyboard) throws TelegramApiException {
        bot.execute(build(chatId, messageId, fileId, text, keyboard));
    }

    private EditMessageMedia build(Long chatId, Integer messageId, String fileId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(chatId.toString());
        editMessageMedia.setMessageId(messageId);
        editMessageMedia.setMedia(new InputMediaPhoto(fileId));
        editMessageMedia.setReplyMarkup(keyboard);
        editMessageMedia.getMedia().setCaption(text);
        editMessageMedia.getMedia().setParseMode("HTML");
        return editMessageMedia;
    }

    /**
     * Изменение подписи в чате с пользователем
     *
     * @param chatId    чат айди пользователя
     * @param messageId идентификатор изменяемого сообщения
     * @param text      текст
     * @param keyboard  inline клавиатура
     */
    public void sendEditMessageCaption(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        try {
            EditMessageCaption editMessageCaption = new EditMessageCaption();
            editMessageCaption.setChatId(chatId.toString());
            editMessageCaption.setMessageId(messageId);
            editMessageCaption.setCaption(text);
            editMessageCaption.setParseMode("HTML");
            editMessageCaption.setReplyMarkup(keyboard);
            bot.execute(editMessageCaption);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки EditMessageCaption: ", e);
        }
    }

    /**
     * Отправка файла без клавиатуры
     *
     * @param chatId  чат айди пользователя
     * @param caption подпись к файлу
     * @param fileId  telegram file id файла
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Optional<Message> sendFile(Long chatId, String caption, String fileId) {
        return sendFile(chatId, new InputFile(fileId), caption);
    }

    /**
     * Отправка файла без клавиатуры и подписи
     *
     * @param chatId чат айди пользователя
     * @param file   отправляемый файл
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Optional<Message> sendFile(Long chatId, File file) {
        return sendFile(chatId, new InputFile(file), null);
    }

    /**
     * Отправка файла без клавиатуры и подписи
     *
     * @param chatId    чат айди пользователя
     * @param inputFile отправляемый файл
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Optional<Message> sendFile(Long chatId, InputFile inputFile) {
        return sendFile(chatId, inputFile, null);
    }

    /**
     * Отправка файла без клавиатуры
     *
     * @param chatId    чат айди пользователя
     * @param inputFile отправляемый файл
     * @param caption   подпись к файлу
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Optional<Message> sendFile(Long chatId, InputFile inputFile, String caption) {
        return sendFile(chatId, inputFile, caption, null);
    }

    /**
     * Отправка файла
     *
     * @param chatId        чат айди пользователя
     * @param inputFile     отправляемый файл
     * @param caption       подпись к файлу
     * @param replyKeyboard клавиатура
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Optional<Message> sendFile(Long chatId, InputFile inputFile, String caption, ReplyKeyboard replyKeyboard) {
        try {
            return Optional.of(
                    bot.execute(SendDocument.builder()
                            .chatId(chatId.toString())
                            .document(inputFile)
                            .caption(caption)
                            .parseMode("html")
                            .replyMarkup(replyKeyboard)
                            .build())
            );
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendDocument", e);
            return Optional.empty();
        }
    }

    /**
     * Отправка видео без клавиатуры
     *
     * @param chatId        чат айди пользователя
     * @param inputFile     отправляемый файл
     * @param caption       подпись к файлу
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Message sendVideo(Long chatId, InputFile inputFile, String caption) {
        return sendVideo(chatId, inputFile, caption, null);
    }

    /**
     * Отправка видео
     *
     * @param chatId        чат айди пользователя
     * @param inputFile     отправляемый файл
     * @param caption       подпись к файлу
     * @param replyKeyboard клавиатура
     * @return сообщение, в случае если оно было успешно доставлено
     */
    public Message sendVideo(Long chatId, InputFile inputFile, String caption, ReplyKeyboard replyKeyboard) {
        try {
            return bot.execute(SendVideo.builder()
                    .chatId(chatId)
                    .video(inputFile)
                    .caption(caption)
                    .replyMarkup(replyKeyboard)
                    .parseMode("HTML")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendVideo", e);
            return null;
        }
    }

    /**
     * Отправка ответа на нажатие inline query кнопки
     * @param inlineQueryId идентфиикатор {@link org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery}
     * @param title заголовок
     * @param description описание
     * @param messageText текста
     */
    public void sendAnswerInlineQuery(String inlineQueryId, String title, String description, String messageText) {
        try {
            bot.execute(AnswerInlineQuery.builder().inlineQueryId(inlineQueryId)
                    .result(InlineQueryResultArticle.builder()
                            .id(inlineQueryId)
                            .title(title)
                            .inputMessageContent(InputTextMessageContent.builder()
                                    .messageText(messageText)
                                    .parseMode("html")
                                    .build())
                            .description(description)
                            .build())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки AnswerInlineQuery", e);
        }
    }

    /**
     * Отправка ответа на нажатие inline кнопки
     * @param callbackQueryId идентфиикатор {@link org.telegram.telegrambots.meta.api.objects.CallbackQuery}
     * @param text текст сообщения
     * @param showAlert true для окна с подтверждением, false для всплывающего окна
     */
    public void sendAnswerCallbackQuery(String callbackQueryId, String text, boolean showAlert) {
        try {
            bot.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQueryId)
                    .text(text)
                    .showAlert(showAlert)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки AnswerCallbackQuery", e);
        }
    }

    /**
     * Отправка группы медиа
     * @param chatId чат айди пользователя
     * @param medias список медиа для отправки
     */
    public void sendMedia(Long chatId, List<InputMedia> medias) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId.toString());
        sendMediaGroup.setMedias(medias);
        try {
            bot.execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendMediaGroup", e);
        }
    }

    /**
     * Изменение клавиатуры сообщения
     * @param chatId чат айди пользователя
     * @param messageId идентификатор сообщения в чате с пользователем
     * @param keyboard новая клавиатура
     */
    public void sendEditMessageReplyMarkup(Long chatId, Integer messageId, InlineKeyboardMarkup keyboard) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setChatId(chatId);
        replyMarkup.setMessageId(messageId);
        replyMarkup.setReplyMarkup(keyboard);
        try {
            bot.execute(replyMarkup);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки EditMessageReplyMarkup", e);
        }
    }

    /**
     * Отправка сообщения пользователю
     * @param botApiMethodMessage отправляемое сообщение
     */
    public void execute(BotApiMethodMessage botApiMethodMessage) {
        try {
            bot.execute(botApiMethodMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки BotApiMethodMessage", e);
        }
    }
}
