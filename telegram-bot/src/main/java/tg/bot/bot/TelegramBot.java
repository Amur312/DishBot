package tg.bot.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.config.BotConfig;
import tg.bot.handlers.CommandDispatcher;
import tg.bot.handlers.Impl.UpdateHandler;


import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CommandDispatcher dispatcher;

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       List<UpdateHandler> handlers, CommandDispatcher dispatcher) {
        this.botConfig = botConfig;

        this.dispatcher = dispatcher;
        handlers.forEach(handler -> dispatcher.registerHandler(handler.getCommand(), handler));
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.dispatch(update);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
