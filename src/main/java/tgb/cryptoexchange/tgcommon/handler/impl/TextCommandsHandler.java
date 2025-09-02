package tgb.cryptoexchange.tgcommon.handler.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;
import tgb.cryptoexchange.tgcommon.handler.TextCommandHandler;
import tgb.cryptoexchange.tgcommon.handler.TextHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TextCommandsHandler implements TextHandler {

    private final Map<String, TextCommandHandler> textCommandHandlerMap = new HashMap<>();

    public TextCommandsHandler(List<TextCommandHandler> commandHandlers) {
        for (TextCommandHandler commandHandler : commandHandlers) {
            textCommandHandlerMap.put(commandHandler.getTextCommand(), commandHandler);
        }
    }

    @Override
    public boolean handle(Message message) {
        TextCommandHandler handler = textCommandHandlerMap.get(message.getText());
        if (Objects.nonNull(handler)) {
            handler.handle(message);
            return true;
        }
        return false;
    }

    @Override
    public TextMessageType getTextMessageType() {
        return TextMessageType.TEXT_COMMAND;
    }
}
