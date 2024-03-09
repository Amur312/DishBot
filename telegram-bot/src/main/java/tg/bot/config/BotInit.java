package tg.bot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bot.bot.TelegramBot;

@Component
@Configuration
public class BotInit {
    Logger logger = LoggerFactory.getLogger(BotInit.class);

    @Autowired
    private TelegramBot bot;

    // Здесь мы внедряем URL вебхука из настроек приложения
    @Value("${ngrok.url}")
    private String webhookUrl;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Устанавливаем вебхук с URL, предоставленным Ngrok
            SetWebhook setWebhook = SetWebhook.builder().url(webhookUrl).build();
            bot.execute(setWebhook);

            telegramBotsApi.registerBot(bot);
            logger.info("Вебхук установлен на {}", webhookUrl);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при регистрации бота!", e);
            throw e;
        }
    }
}
