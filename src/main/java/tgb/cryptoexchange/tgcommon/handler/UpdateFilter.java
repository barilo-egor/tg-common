package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateFilter {

    void handle(Update update);

    boolean match(Update update);
}
