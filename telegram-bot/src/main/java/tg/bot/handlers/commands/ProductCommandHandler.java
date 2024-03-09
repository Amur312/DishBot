package tg.bot.handlers.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.Product;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.ProductService;
import tg.bot.util.ConvertEmojiToCommand;

import java.util.List;
@Component
@Slf4j
public class ProductCommandHandler implements UpdateHandler {
    private final ProductService productService;
    private final AbsSender absSender;
    private final ConvertEmojiToCommand utilEmoji;
    public ProductCommandHandler(ProductService productService, @Lazy AbsSender absSender, ConvertEmojiToCommand utilEmoji) {
        this.productService = productService;
        this.absSender = absSender;
        this.utilEmoji = utilEmoji;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.PRODUCTS;
    }

    @Override
    public void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        try {
            List<Product> products = productService.findAllProducts();
            StringBuilder messageText = new StringBuilder("Список продуктов:\n");
            for (Product product : products) {
                messageText.append(String.format("• %s - %s\n", product.getName(), product.getPrice()));
            }
            SendMessage message = new SendMessage(String.valueOf(chatId), messageText.toString());
            absSender.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при обработке команды /products", e);
        }
    }


    @Override
    public boolean canHandleUpdate(Update update) {
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return update.hasMessage() && update.getMessage().hasText() &&
                (convertedCommand.equalsIgnoreCase("/products"));
    }
}
