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

    public String buildData(CallbackQueryData data, String... arguments) {
        return data.getData() + SPLITTER + String.join(SPLITTER, arguments);
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

    public String getArgument(String data, int index) {
        String[] split = data.split(SPLITTER);
        if (split.length - 1 < index) {
            return null;
        }
        return split[index];
    }

    public Long getLongArgument(String data, int index) {
        String argument = getArgument(data, index);
        if (argument == null) {
            return null;
        }
        try {
            return Long.parseLong(argument);
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Long: data=%s, index=%s".formatted(data, index), e);
        }
    }

    public Integer getIntArgument(String data, int index) {
        String argument = getArgument(data, index);
        if (argument == null) {
            return null;
        }
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new TelegramCommonException("Ошибка при парсинге к Integer: data=" + data + ", index=" + index, e);
        }
    }

    public Set<Integer> getIntArguments(String data) {
        String[] split = data.split(SPLITTER);
        Set<Integer> result = new HashSet<>();
        if (split.length <= 1) {
            return result;
        }
        for (int i = 1; i < split.length; i++) {
            result.add(Integer.parseInt(split[i]));
        }
        return result;
    }

    public Boolean getBoolArgument(String data, int index) {
        String argument = getArgument(data, index);
        if (argument == null) {
            throw new NullPointerException("No boolean argument found");
        }
        if (Boolean.TRUE.toString().equalsIgnoreCase(argument)) {
            return true;
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(argument)) {
            return false;
        }
        throw new TelegramCommonException("Ошибка при парсинге к Boolean: data=" + data + ", index=" + index);
    }

    public boolean isCallbackQueryData(CallbackQueryData callbackQueryData, String data) {
        CallbackQueryData fromData = fromData(data);
        if (fromData == null) {
            return false;
        }
        return fromData.equals(callbackQueryData);
    }

    public boolean hasArguments(String data) {
        if (Objects.isNull(data)) {
            return false;
        }
        String[] split = data.split(SPLITTER);
        return split.length > 1;
    }
}
