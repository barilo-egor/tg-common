package tgb.cryptoexchange.tgcommon.service.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tgb.cryptoexchange.tgcommon.bot.MethodExecutor;

@Component
@Slf4j
public class ResponseSender {

    private final MethodExecutor methodExecutor;

    public ResponseSender(MethodExecutor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }

    public MessageTypeResolver to(Long chatId) {
        return new MessageTypeResolver(methodExecutor, chatId);
    }

    public AnswerToInlineQuery answerToInlineQuery(Integer inlineQueryId) {
        return new AnswerToInlineQuery(methodExecutor, inlineQueryId);
    }
}
