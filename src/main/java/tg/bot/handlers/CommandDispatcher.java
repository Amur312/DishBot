package tg.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.IMessageHandler;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.User;
import tg.bot.model.enums.BotState;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CallbackQueryHandler;
import tg.bot.service.CatalogService;
import tg.bot.service.CategoryService;
import tg.bot.service.UserService;
import tg.bot.util.ConvertEmojiToCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CommandDispatcher {
    private final Map<CommandBot, UpdateHandler> handlers = new HashMap<>();
    private final UserService userService;
    private final ConvertEmojiToCommand utilEmoji;
    private final AbsSender absSender;
    private final Map<Class<?>, IMessageHandler> messageHandlers = new HashMap<>();
    private final CategoryService categoryService;
    private final CatalogService catalogService;
    @Autowired
    public CommandDispatcher(UserService userService, ConvertEmojiToCommand utilEmoji, @Lazy AbsSender absSender, List<IMessageHandler> messageHandlersList, CategoryService categoryService, CatalogService catalogService) {
        this.userService = userService;
        this.utilEmoji = utilEmoji;
        this.absSender = absSender;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        messageHandlersList.forEach(handler -> messageHandlers.put(handler.getClass(), handler));
    }

    public void registerHandler(CommandBot command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQueryHandler callbackHandler = new CallbackQueryHandler(absSender, categoryService,catalogService);
            callbackHandler.handleCallbackQuery(update);
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasContact()) {
                IMessageHandler contactHandler = messageHandlers.get(ContactMessageHandler.class);
                log.info("contactHandler = " + contactHandler);
                if (contactHandler != null) {
                    contactHandler.handle(message);
                }
            } else if (message.hasText()) {
                User user = userService.findByChatId(message.getChatId()).orElse(null);
                if (user != null && (user.getState() == BotState.AWAITING_FIRST_NAME || user.getState() == BotState.AWAITING_LAST_NAME)) {
                    IMessageHandler textHandler = messageHandlers.get(UserRegistrationHandler.class);
                    if (textHandler != null) {
                        textHandler.handle(message);
                    }
                } else {
                    handleTextMessage(update, message);
                }
            }
        }
    }



    private void handleTextMessage(Update update, Message message) {
        String commandText = message.getText().split(" ")[0];

        if (!commandText.startsWith("/")) {
            commandText = utilEmoji.convertEmojiToCommand(commandText);
        }
        if (commandText.startsWith("/")) {
            commandText = commandText.substring(1).toUpperCase();
            try {
                CommandBot command = CommandBot.valueOf(commandText);
                UpdateHandler handler = handlers.get(command);
                if (handler != null && handler.canHandleUpdate(update)) {
                    handler.handleUpdate(update);
                }
            } catch (IllegalArgumentException e) {
                log.warn("Неизвестная команда: {}", commandText);
            }
        }
    }

}

