package tg.bot.bot;



import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.config.BotConfig;
import tg.bot.handlers.CommandDispatcher;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.repository.UserRepository;
import tg.bot.service.UserService;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserRepository userRepository;
    private final BotConfig botConfig;
    private final CommandDispatcher dispatcher;

    public TelegramBot(BotConfig botConfig, UserRepository userRepository, UserService userService, List<UpdateHandler> handlers) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        this.dispatcher = new CommandDispatcher(userService);
        handlers.forEach(handler -> dispatcher.registerHandler(handler.getCommand(), handler));
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.dispatch(update);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.getMessage();
        }
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
