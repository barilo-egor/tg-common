package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public record Action(MethodExecutor methodExecutor, Long chatId) {

    public void typing() {
        execute("typing");
    }

    public void chooseSticker() {
        execute("choose_sticker");
    }

    public void uploadPhoto() {
        execute("upload_photo");
    }

    public void uploadVideo() {
        execute("upload_video");
    }

    public void uploadDocument() {
        execute("upload_document");
    }

    public void uploadVoice() {
        execute("upload_voice");
    }

    public void uploadVideoNote() {
        execute("upload_video_note");
    }

    public void recordVideo() {
        execute("record_video");
    }

    public void recordVoice() {
        execute("record_voice");
    }

    public void recordVideoNote() {
        execute("record_video_note");
    }

    public void findLocation() {
        execute("find_location");
    }

    private void execute(String action) {
        methodExecutor.execute(
                SendChatAction.builder()
                        .chatId(chatId)
                        .action(action)
                        .build()
        );
    }
}
