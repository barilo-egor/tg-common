package tgb.cryptoexchange.tgcommon.handler;

public interface BotExceptionHandler {

    boolean isInstance(Exception e);

    void handle(Exception e);
}