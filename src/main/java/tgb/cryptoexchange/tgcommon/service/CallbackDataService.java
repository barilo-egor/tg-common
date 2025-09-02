package tgb.cryptoexchange.tgcommon.service;

import org.springframework.stereotype.Service;
import tgb.cryptoexchange.tgcommon.constants.CallbackQueryData;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;
import tgb.cryptoexchange.tgcommon.handler.CallbackQueryHandler;

import java.util.*;

@Service
public class CallbackDataService {

    public static final String SPLITTER = ":";

    private final Map<String, CallbackQueryData> callbackQueryHandlers;

    public CallbackDataService(List<CallbackQueryHandler> handlers) {
        callbackQueryHandlers = new HashMap<>();
        for (CallbackQueryHandler handler : handlers) {
            callbackQueryHandlers.put(handler.getCallbackQueryData().getData(), handler.getCallbackQueryData());
        }
    }

    public CallbackQueryData fromData(String data) {
        if (Objects.isNull(data) || data.isBlank()) {
            return null;
        }
        String[] split = data.split(SPLITTER);
        if (split.length < 1) {
            return null;
        }
        try {
            return callbackQueryHandlers.get(split[0]);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
