package tgb.cryptoexchange.tgcommon.service.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.tgcommon.bot.BotInstance;

@ExtendWith(MockitoExtension.class)
class ResponseSenderTest {

    @Mock
    private BotInstance bot;

    @InjectMocks
    private ResponseSender responseSender;

    @Test
    void should() {
        responseSender.to(1L).message("some message").send();
    }

}