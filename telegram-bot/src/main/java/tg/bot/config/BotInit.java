package tg.bot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bot.bot.TelegramBot;

@Component
@Configuration
public class BotInit {
    Logger logger = LoggerFactory.getLogger(BotInit.class);
    @Autowired
    private TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void inti() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            logger.error("Ошибка регистрации!", e);
            throw new RuntimeException(e);
        }
    }
}

