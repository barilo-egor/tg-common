package tgb.cryptoexchange.tgcommon.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BotInstanceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private BotInstance botInstance;

    @ParameterizedTest
    @ValueSource(ints = {1, 123, 532234})
    @DisplayName("onUpdateReceived(Update update) - передан Update - updateId совпадает с тем, что в event")
    void shouldPublishEventWhenUpdateReceived(Integer updateId) {
        Update update = new Update();
        update.setUpdateId(updateId);
        botInstance.onUpdateReceived(update);
        ArgumentCaptor<TelegramUpdateEvent> captor = ArgumentCaptor.forClass(TelegramUpdateEvent.class);
        verify(applicationEventPublisher, times(1)).publishEvent(captor.capture());
        TelegramUpdateEvent telegramUpdateEvent = captor.getValue();
        assertEquals(updateId, telegramUpdateEvent.getUpdate().getUpdateId());
        assertEquals(botInstance, telegramUpdateEvent.getSource());
    }

    @ParameterizedTest
    @ValueSource(strings = {"username1bot", "somebot", "qwerty123bot"})
    void shouldSetUsername(String username) {
        String token = "token";
        BotInstance instance = new BotInstance(token, username, applicationEventPublisher);
        assertEquals(username, instance.getBotUsername());
    }
}