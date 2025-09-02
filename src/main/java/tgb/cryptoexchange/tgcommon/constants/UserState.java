package tgb.cryptoexchange.tgcommon.constants;

/**
 * Представление состояния пользователя. Используется для ведения диалога с пользователем.<br>
 * Сохраняется с помощью Redis через {@link tgb.cryptoexchange.tgcommon.service.RedisUserStateService}
 * Самый простой вариант реализации - перечисление.
 */
public interface UserState {

    String getState();
}
