package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UserState;

public interface StateHandler {

    void handle(Update update);

    UserState getUserState();
}
