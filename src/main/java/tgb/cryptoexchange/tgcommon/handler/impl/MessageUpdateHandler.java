package tgb.cryptoexchange.tgcommon.handler.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.MessageType;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.handler.MessageHandler;
import tgb.cryptoexchange.tgcommon.handler.UpdateHandler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MessageUpdateHandler implements UpdateHandler {

    private final Map<MessageType, MessageHandler> messageHandlerMap = new EnumMap<>(MessageType.class);
    public MessageUpdateHandler(List<MessageHandler> messageHandlers) {
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandlerMap.put(messageHandler.getMessageType(), messageHandler);
        }
    }

    @Override
    public boolean handle(Update update) {
        Message message = update.getMessage();
        MessageType messageType = MessageType.fromMessage(message);
        MessageHandler messageHandler = messageHandlerMap.get(messageType);
        if (Objects.nonNull(messageHandler)) {
            return messageHandler.handleMessage(message);
        }
        return false;
    }

    @Override
    public UpdateType getUpdateType() {
        return UpdateType.MESSAGE;
    }
}
