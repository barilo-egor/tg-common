package tgb.cryptoexchange.tgcommon.handler.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.handler.CallbackQueryHandler;
import tgb.cryptoexchange.tgcommon.handler.UpdateHandler;
import tgb.cryptoexchange.tgcommon.keyboard.PressedInlineButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CallbackQueriesHandler implements UpdateHandler {

    private final Map<String, CallbackQueryHandler> callbackQueryHandlerMap = new HashMap<>();

    public CallbackQueriesHandler(List<CallbackQueryHandler> callbackQueryHandlers) {
        for (CallbackQueryHandler handler : callbackQueryHandlers) {
            callbackQueryHandlerMap.put(handler.getId(), handler);
        }
    }

    @Override
    public boolean handle(Update update) {
        var pressedButton = PressedInlineButton.build(update.getCallbackQuery());
        CallbackQueryHandler callbackQueryHandler = callbackQueryHandlerMap.get(pressedButton.getArgument(0));
        if (Objects.isNull(callbackQueryHandler)) return false;
        callbackQueryHandler.handle(pressedButton);
        return true;
    }

    @Override
    public UpdateType getUpdateType() {
        return UpdateType.CALLBACK_QUERY;
    }
}
