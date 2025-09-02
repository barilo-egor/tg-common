package tgb.cryptoexchange.tgcommon.handler.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.MessageType;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;
import tgb.cryptoexchange.tgcommon.handler.MessageHandler;
import tgb.cryptoexchange.tgcommon.handler.TextHandler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TextMessageHandler implements MessageHandler {

    private final Map<TextMessageType, TextHandler> textMessageHandlersMap = new EnumMap<>(TextMessageType.class);

    public TextMessageHandler(List<TextHandler> textMessageHandlers) {
        for (TextHandler textMessageHandler : textMessageHandlers) {
            textMessageHandlersMap.put(textMessageHandler.getTextMessageType(), textMessageHandler);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        TextMessageType textMessageType = TextMessageType.fromString(message.getText());
        if (Objects.nonNull(textMessageType)) {
            TextHandler textMessageHandler = textMessageHandlersMap.get(textMessageType);
            if (textMessageHandler != null) {
                return textMessageHandler.handle(message);
            }
        }
        return false;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TEXT;
    }

}
