package tgb.cryptoexchange.tgcommon.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import tgb.cryptoexchange.tgcommon.constants.UpdateFilterType;

public interface UpdateFilterService {

    UpdateFilterType getType(Update update);
}
