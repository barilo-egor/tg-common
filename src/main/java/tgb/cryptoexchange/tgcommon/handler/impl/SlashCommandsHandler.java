package tgb.cryptoexchange.tgcommon.handler.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import tgb.cryptoexchange.tgcommon.constants.TextMessageType;
import tgb.cryptoexchange.tgcommon.handler.SlashCommandHandler;
import tgb.cryptoexchange.tgcommon.handler.TextHandler;

import java.util.*;

@Service
public class SlashCommandsHandler implements TextHandler {

    private final Map<String, SlashCommandHandler> handlers = new HashMap<>();

    public SlashCommandsHandler(List<SlashCommandHandler> slashCommandHandlers) {
        for (SlashCommandHandler handler : slashCommandHandlers) {
            handlers.put(handler.getSlashCommand().replace("/", ""), handler);
        }
    }

    public boolean handle(Message message) {
        SlashCommandHandler slashCommandHandler = handlers.get(message.getText().replace("/", ""));
        if (Objects.nonNull(slashCommandHandler)) {
            slashCommandHandler.handle(message);
            return true;
        }
        return false;
    }

    @Override
    public TextMessageType getTextMessageType() {
        return TextMessageType.SLASH_COMMAND;
    }
}
