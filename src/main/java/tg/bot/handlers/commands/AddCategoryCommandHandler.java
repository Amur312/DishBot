package tg.bot.handlers.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.Category;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CategoryService;

import static tg.bot.util.MessageUtils.sendMessage;

@Component
public class AddCategoryCommandHandler implements UpdateHandler {
    private final AbsSender absSender;
    private final CategoryService categoryService;

    @Autowired
    public AddCategoryCommandHandler(@Lazy AbsSender absSender, CategoryService categoryService) {
        this.absSender = absSender;
        this.categoryService = categoryService;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.ADD_CATEGORY;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            String[] parts = text.split(" ", 2);
            if (parts.length == 2) {
                String categoryName = parts[1];
                try {
                    Category newCategory = categoryService.createRootCategory(categoryName);
                    sendMessage(absSender, chatId, "Родительская категория '" + newCategory.getName() + "' успешно создана.");
                } catch (Exception e) {
                    sendMessage(absSender, chatId, "Ошибка при создании категории: " + e.getMessage());
                }
            } else {
                sendMessage(absSender, chatId, "Пожалуйста, укажите название категории после команды /addcategory.");
            }
        }
    }


    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().toLowerCase().startsWith("/add_category");
    }

}
