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
import tg.bot.model.Client;
import tg.bot.model.enums.BotState;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.*;
import tg.bot.util.ConvertEmojiToCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tg.bot.util.MessageUtils.handleTextMessage;

@Slf4j
@Component
public class CommandDispatcher {
    private final Map<CommandBot, UpdateHandler> handlers = new HashMap<>();
    private final ClientService userService;
    private final ConvertEmojiToCommand utilEmoji;
    private final AbsSender absSender;
    private final Map<Class<?>, IMessageHandler> messageHandlers = new HashMap<>();
    private final CategoryService categoryService;
    private final CatalogService catalogService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public CommandDispatcher(ClientService userService, ConvertEmojiToCommand utilEmoji, @Lazy AbsSender absSender,
                             List<IMessageHandler> messageHandlersList, CategoryService categoryService,
                             CatalogService catalogService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.utilEmoji = utilEmoji;
        this.absSender = absSender;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        this.productService = productService;
        this.orderService = orderService;
        messageHandlersList.forEach(handler -> messageHandlers.put(handler.getClass(), handler));
    }

    public void registerHandler(CommandBot command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQueryHandler callbackHandler = new CallbackQueryHandler(absSender, categoryService, catalogService,
                    productService, orderService);
            callbackHandler.handleCallbackQuery(update);
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasContact()) {
                IMessageHandler contactHandler = messageHandlers.get(ContactMessageHandler.class);
                if (contactHandler != null) {
                    contactHandler.handle(message);
                }
            } else if (message.hasText()) {
                Client user = userService.findByChatId(message.getChatId()).orElse(null);
                if (user != null && (user.getState() == BotState.AWAITING_FIRST_NAME || user.getState() == BotState.AWAITING_LAST_NAME)) {
                    IMessageHandler textHandler = messageHandlers.get(UserRegistrationHandler.class);
                    if (textHandler != null) {
                        textHandler.handle(message);
                    }
                } else {
                    handleTextMessage(update, message, handlers, utilEmoji);
                }
            }
        }
    }
}

