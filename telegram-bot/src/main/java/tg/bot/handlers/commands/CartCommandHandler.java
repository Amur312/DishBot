package tg.bot.handlers.commands;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.OrderItem;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CartService;
import tg.bot.util.ConvertEmojiToCommand;


import java.util.List;

import static tg.bot.util.MessageUtils.sendMessage;

@Component
public class CartCommandHandler implements UpdateHandler {
    private final CartService cartService;
    private final ConvertEmojiToCommand utilEmoji;
    private final AbsSender absSender;

    public CartCommandHandler(CartService cartService, ConvertEmojiToCommand utilEmoji, @Lazy AbsSender absSender) {
        this.cartService = cartService;
        this.utilEmoji = utilEmoji;
        this.absSender = absSender;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();

            List<OrderItem> cartItems = cartService.getCartItems(chatId);
            StringBuilder messageText = new StringBuilder("Товары в вашей корзине:\n");
            for (OrderItem item : cartItems) {
                messageText.append("- ").append(item.getProductName()).append(": ")
                        .append(item.getProductPrice()).append(" UZS\n");
            }
            try {
                sendMessage(absSender, chatId, messageText.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean canHandleUpdate(Update update) {
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return update.hasMessage() && update.getMessage().hasText() &&
                (convertedCommand.equalsIgnoreCase("/basket"));
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.BASKET;
    }
}
