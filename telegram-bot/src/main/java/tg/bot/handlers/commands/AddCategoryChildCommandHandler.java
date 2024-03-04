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
public class AddCategoryChildCommandHandler implements UpdateHandler {
    private final CategoryService categoryService;
    private final AbsSender absSender;
    @Autowired
    public AddCategoryChildCommandHandler(CategoryService categoryService, @Lazy AbsSender absSender) {
        this.categoryService = categoryService;
        this.absSender = absSender;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.ADD_CHILD_CATEGORY;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            String[] parts = text.split(" ", 3);
            if (parts.length < 3) {
                sendMessage(absSender, chatId, "Использование: /add_child_category ДочерняяКатегория РодительскаяКатегория");
                return;
            }
            String childName = parts[1];
            String parentName = parts[2];
            try {
                Category parentCategory = categoryService.findByName(parentName);
                if (parentCategory == null) {
                    sendMessage(absSender, chatId, "Родительская категория '" + parentName + "' не найдена.");
                    return;
                }
                Category childCategory = categoryService.createChildCategory(childName, parentCategory.getId());
                sendMessage(absSender, chatId, "Категория '" + childCategory.getName() + "' была успешно добавлена как подкатегория к '" + parentName + "'.");
            } catch (Exception e) {
                sendMessage(absSender, chatId, "Произошла ошибка при добавлении категории: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().toLowerCase().startsWith("/add_child_category");
    }

}
