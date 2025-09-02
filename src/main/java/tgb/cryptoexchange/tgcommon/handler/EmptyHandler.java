package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;

public interface EmptyHandler {

    BotApiMethodMessage getEmptyMessage(Long chatId);
}
