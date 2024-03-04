package tg.bot.handlers.commands;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CategoryService;

import static tg.bot.util.MessageUtils.sendMessage;

@Component
public class DeleteCategoryCommandHandler implements UpdateHandler {
    private final CategoryService categoryService;
    private final AbsSender absSender;

    @Autowired
    public DeleteCategoryCommandHandler(CategoryService categoryService, @Lazy AbsSender absSender) {
        this.categoryService = categoryService;
        this.absSender = absSender;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.DELETE_CATEGORY;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            String[] parts = text.split(" ", 2);

            if (parts.length < 2) {
                sendMessage(absSender, chatId, "Использование: /delete_category [id категории]");
                return;
            }
            try {
                Long categoryId = Long.parseLong(parts[1]);
                categoryService.deleteCategoryAndChildren(categoryId);
                sendMessage(absSender, chatId, "Категория с ID " + categoryId + " и все ее подкатегории были успешно удалены.");
            } catch (NumberFormatException e) {
                sendMessage(absSender, chatId, "ID категории должно быть числом. Пожалуйста, укажите корректный ID.");
            } catch (EntityNotFoundException e) {
                sendMessage(absSender, chatId, "Категория не найдена.");
            } catch (Exception e) {
                sendMessage(absSender, chatId, "Произошла ошибка при удалении категории: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().toLowerCase().startsWith("/delete_category");
    }
}
