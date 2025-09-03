package tgb.cryptoexchange.tgcommon.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import tgb.cryptoexchange.tgcommon.constants.UpdateType;
import tgb.cryptoexchange.tgcommon.constants.UserState;
import tgb.cryptoexchange.tgcommon.exception.HandlerTypeNotFoundException;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;
import tgb.cryptoexchange.tgcommon.handler.*;
import tgb.cryptoexchange.tgcommon.service.RedisUserStateService;
import tgb.cryptoexchange.tgcommon.service.sender.ResponseSender;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateEventListenerTest {

    @Mock
    private RedisUserStateService redisUserStateService;

    @Mock
    private EmptyHandler emptyHandler;

    @Mock
    private ObjectProvider<AntiSpam> antiSpamProvider;

    @Mock
    private ObjectProvider<BannedCache> bannedCacheProvider;

    @Mock
    private ResponseSender responseSender;

    @Mock
    private AntiSpam antiSpam;

    @Mock
    private BannedCache bannedCache;

    @Test
    @DisplayName("new TelegramUpdateEventListener - отсутствует antiSpam - проброшен TelegramCommonException")
    void shouldThrowTelegramCommonExceptionIfAntiSpamIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(null);
        assertThrows(TelegramCommonException.class, () -> new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        ));
    }


    @Test
    @DisplayName("new TelegramUpdateEventListener - отсутствует antiSpam - проброшен TelegramCommonException")
    void shouldThrowTelegramCommonExceptionIfBannedCacheIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(null);
        assertThrows(TelegramCommonException.class, () -> new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        ));
    }

    @Test
    @DisplayName("new TelegramUpdateEventListener - один из updateType == null - проброшен HandlerTypeNotFoundException")
    void shouldThrowTelegramCommonExceptionIfUpdateTypeOfUpdateHandlerIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);

        List<UpdateHandler> mockedUpdateHandlers = new ArrayList<>();
        UpdateHandler updateHandlerWithNullUpdateType = Mockito.mock(UpdateHandler.class);
        when(updateHandlerWithNullUpdateType.getUpdateType()).thenReturn(null);
        UpdateHandler updateHandlerWithNonNullUpdateType = Mockito.mock(UpdateHandler.class);
        when(updateHandlerWithNonNullUpdateType.getUpdateType()).thenReturn(UpdateType.MESSAGE);
        mockedUpdateHandlers.add(updateHandlerWithNonNullUpdateType);
        mockedUpdateHandlers.add(updateHandlerWithNullUpdateType);

        assertThrows(HandlerTypeNotFoundException.class, () -> new TelegramUpdateEventListener(
                redisUserStateService, mockedUpdateHandlers, new ArrayList<>(),
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        ));
    }

    @Test
    @DisplayName("new TelegramUpdateEventListener - один из userState == null - проброшен HandlerTypeNotFoundException")
    void shouldThrowTelegramCommonExceptionIfUserStateOfStateHandlerIsNull() {
        when(antiSpamProvider.getIfAvailable()).thenReturn(antiSpam);
        when(bannedCacheProvider.getIfAvailable()).thenReturn(bannedCache);

        List<StateHandler> mockedStateHandlers = new ArrayList<>();
        StateHandler updateHandlerWithNullUpdateType = Mockito.mock(StateHandler.class);
        when(updateHandlerWithNullUpdateType.getUserState()).thenReturn(null);
        StateHandler updateHandlerWithNonNullUpdateType = Mockito.mock(StateHandler.class);
        when(updateHandlerWithNonNullUpdateType.getUserState()).thenReturn(() -> "someState");
        mockedStateHandlers.add(updateHandlerWithNonNullUpdateType);
        mockedStateHandlers.add(updateHandlerWithNullUpdateType);

        assertThrows(HandlerTypeNotFoundException.class, () -> new TelegramUpdateEventListener(
                redisUserStateService, new ArrayList<>(), mockedStateHandlers,
                emptyHandler, new ArrayList<>(), antiSpamProvider, bannedCacheProvider, responseSender
        ));
    }

}