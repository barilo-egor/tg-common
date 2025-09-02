package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tgb.cryptoexchange.tgcommon.constants.CallbackQueryData;

public interface CallbackQueryHandler {

    void handle(CallbackQuery callbackQuery);

    CallbackQueryData getCallbackQueryData();
}
