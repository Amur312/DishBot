package tg.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:/application.properties")
public class BotConfig {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.ownerUserName}")
    private String botOwnerUserName;

    public BotConfig() {

    }
    public BotConfig(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.name}")
    String botName) {
        this.botToken = botToken;
        this.botName = botName;
    }

}
