package tgb.cryptoexchange.tgcommon.handler;


import tgb.cryptoexchange.tgcommon.constants.StartParameter;

public interface StartParameterHandler {

    void handle(Long chatId, String value);

    StartParameter getParameter();
}
