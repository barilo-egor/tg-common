package tgb.cryptoexchange.tgcommon.service.sender;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

public class AnswerToCallbackQuery {

    private final MethodExecutor methodExecutor;

    private final String text;

    private final String callbackQueryId;

    private boolean showAlert;

    public AnswerToCallbackQuery(MethodExecutor methodExecutor, String text, String callbackQueryId) {
        this.methodExecutor = methodExecutor;
        this.text = text;
        this.callbackQueryId = callbackQueryId;
    }

    public AnswerToCallbackQuery showAlert(boolean showAlert) {
        this.showAlert = showAlert;
        return this;
    }

    public void send() {
        methodExecutor.send(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQueryId)
                        .text(text)
                        .showAlert(showAlert)
                        .build()
        );
    }
}
