package tgb.cryptoexchange.tgcommon.service.sender;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgb.cryptoexchange.tgcommon.bot.BotInstance;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;
import tgb.cryptoexchange.tgcommon.keyboard.InlineButton;
import tgb.cryptoexchange.tgcommon.keyboard.KeyboardBuilder;
import tgb.cryptoexchange.tgcommon.keyboard.ReplyButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ResponseSender {

    private final BotInstance bot;
    private final KeyboardBuilder keyboardBuildService;
    private final String botToken;

    public ResponseSender(BotInstance bot, KeyboardBuilder keyboardBuildService, @Value("${bot.token}") String botToken) {
        this.bot = bot;
        this.keyboardBuildService = keyboardBuildService;
        this.botToken = botToken;
    }

    public Optional<Message> sendMessage(Long chatId, String text) {
        return sendMessage(chatId, text, null, null);
    }

    public Optional<Message> sendMessage(Long chatId, String text, Integer replyToMessageId) {
        return sendMessage(chatId, text, null, replyToMessageId);
    }

    public Optional<Message> sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        return sendMessage(chatId, text, replyKeyboard, null);
    }

    public Optional<Message> sendMessage(Long chatId, String text, InlineButton... inlineButtons) {
        return sendMessage(chatId, text, keyboardBuildService.buildInline(List.of(inlineButtons)), null);
    }

    public Optional<Message> sendMessage(Long chatId, String text, ReplyButton... replyButtons) {
        return sendMessage(chatId, text, keyboardBuildService.buildReply(List.of(replyButtons)), null);
    }

    public Optional<Message> sendMessage(Long chatId, String text, List<InlineButton> buttons) {
        return sendMessage(chatId, text, keyboardBuildService.buildInline(buttons), null);
    }

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

    public void sendMessageThrows(Long chatId, String text) throws TelegramApiException {
        bot.execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("html")
                .build());
    }

    public Optional<Message> sendPhoto(Long chatId, String caption, InputFile photo) {
        return sendPhoto(chatId, caption, photo, null);
    }

    public Optional<Message> sendPhoto(Long chatId, String caption, String photo, ReplyKeyboard replyKeyboard) {
        return sendPhoto(chatId, caption, new InputFile(photo), replyKeyboard);
    }

    public Optional<Message> sendPhoto(Long chatId, String caption, String photo) {
        return sendPhoto(chatId, caption, new InputFile(photo), null);
    }

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

    public Message sendAnimation(Long chatId, File file) {
        return sendAnimation(chatId, new InputFile(file), null, null);
    }

    public Optional<Message> sendAnimation(Long chatId, String caption, String animation, ReplyKeyboard replyKeyboard) {
        return Optional.ofNullable(sendAnimation(chatId, new InputFile(animation), caption, replyKeyboard));
    }

    public Message sendAnimation(Long chatId, InputFile inputFile, String caption, ReplyKeyboard replyKeyboard) {
        try {
            return bot.execute(SendAnimation.builder()
                    .chatId(chatId.toString())
                    .animation(inputFile)
                    .caption(caption)
                    .replyMarkup(replyKeyboard)
                    .parseMode("html")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendAnimation: ", e);
            return null;
        }
    }

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

    public void sendEditedMessageText(Long chatId, Integer messageId, String text) {
        sendEditedMessageText(chatId, messageId, text, (InlineKeyboardMarkup) null);
    }

    public void sendEditedMessageText(Long chatId, Integer messageId, String text, List<InlineButton> buttons) {
        sendEditedMessageText(chatId, messageId, text, keyboardBuildService.buildInline(buttons));
    }

    public void sendEditedMessageText(Long chatId, Integer messageId, String text, ReplyKeyboard replyKeyboard) {
        sendEditedMessageText(chatId, messageId, text, (InlineKeyboardMarkup) replyKeyboard);
    }

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

    public void sendEditedMessageTextThrows(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) throws TelegramApiException {
        bot.execute(EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .parseMode("html")
                .build());
    }

    public void sendEditMessageMedia(Long chatId, Integer messageId, String fileId, String text, InlineKeyboardMarkup keyboard) {
        try {
            bot.execute(build(chatId, messageId, fileId, text, keyboard));
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки EditMessageMedia: ", e);
        }
    }

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

    public void sendEditMessageCaption(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboardMarkup) {
        try {
            EditMessageCaption editMessageCaption = new EditMessageCaption();
            editMessageCaption.setChatId(chatId.toString());
            editMessageCaption.setMessageId(messageId);
            editMessageCaption.setCaption(text);
            editMessageCaption.setParseMode("HTML");
            editMessageCaption.setReplyMarkup(keyboardMarkup);
            bot.execute(editMessageCaption);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки EditMessageCaption: ", e);
        }
    }

    public Message sendFile(Long chatId, String caption, String fileId) {
        return sendFile(chatId, new InputFile(fileId), caption);
    }

    public Message sendFile(Long chatId, File file) {
        return sendFile(chatId, new InputFile(file), null);
    }

    public Message sendFile(Long chatId, InputFile inputFile) {
        return sendFile(chatId, inputFile, null);
    }

    public Message sendFile(Long chatId, InputFile inputFile, String caption) {
        return sendFile(chatId, inputFile, caption, null);
    }

    public Message sendFile(Long chatId, InputFile inputFile, String caption, ReplyKeyboard replyKeyboard) {
        try {
            return bot.execute(SendDocument.builder()
                    .chatId(chatId.toString())
                    .document(inputFile)
                    .caption(caption)
                    .parseMode("html")
                    .replyMarkup(replyKeyboard)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки SendDocument", e);
            return null;
        }
    }

    public Message sendVideo(Long chatId, InputFile inputFile, String caption) {
        return sendVideo(chatId, inputFile, caption, null);
    }

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

    public void downloadFile(Document document, String localFilePath) throws IOException, URISyntaxException {
        org.telegram.telegrambots.meta.api.objects.File file = getFilePath(document);
        java.io.File localFile = new java.io.File(localFilePath);
        URI uri = new URI(file.getFileUrl(botToken));
        InputStream is = uri.toURL().openStream();
        FileUtils.copyInputStreamToFile(is, localFile);
    }

    private org.telegram.telegrambots.meta.api.objects.File getFilePath(Document document) {
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        return execute(getFile);
    }

    private org.telegram.telegrambots.meta.api.objects.File execute(GetFile getFile) {
        try {
            return bot.execute(getFile);
        } catch (TelegramApiException e) {
            throw new TelegramCommonException("Не получилось скачать файл: " + getFile, e);
        }
    }

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

    public void deleteCallbackMessageIfExists(Update update) {
        Long chatId = UpdateType.getChatId(update);
        if (update.hasCallbackQuery())
            deleteMessage(chatId, update.getCallbackQuery().getMessage().getMessageId());
    }

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
}
