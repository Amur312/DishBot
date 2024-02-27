package tg.bot.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.config.BotConfig;
import tg.bot.handlers.CommandDispatcher;
import tg.bot.handlers.Impl.IMessageHandler;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.repository.UserRepository;
import tg.bot.service.UserService;
import tg.bot.util.ConvertEmojiToCommand;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserRepository userRepository;
    private final BotConfig botConfig;
    private final CommandDispatcher dispatcher;
    private final ConvertEmojiToCommand utilEmoji;
    private final AbsSender absSender;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserRepository userRepository, UserService userService,
                       List<UpdateHandler> handlers, List<IMessageHandler> messageHandlers,
                       ConvertEmojiToCommand utilEmoji, @Lazy AbsSender absSender) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        this.utilEmoji = utilEmoji;
        this.absSender = absSender;
        // Инициализация dispatcher с новым списком обработчиков сообщений
        this.dispatcher = new CommandDispatcher(userService, utilEmoji, absSender, messageHandlers);
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
