package tg.bot.handlers.commands;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.ProductService;

@Component
public class AddProductCommandHandler implements UpdateHandler {
    private final AbsSender absSender;
    private final ProductService productService;

    public AddProductCommandHandler(@Lazy AbsSender absSender, ProductService productService) {
        this.absSender = absSender;
        this.productService = productService;
    }


    @Override
    public CommandBot getCommand() {
        return CommandBot.ADD_PRODUCT;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String commandText = update.getMessage().getText();

            System.out.println("commandText -> " + commandText);

            String[] parts = commandText.split(" ",)
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().toLowerCase().startsWith("/add_product");
    }
}
