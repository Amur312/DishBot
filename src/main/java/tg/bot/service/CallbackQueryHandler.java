package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.model.Category;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.util.MessageUtils.sendMessage;

@Component
public class CallbackQueryHandler {
    private final AbsSender absSender;
    private final CategoryService categoryService;
    private final CatalogService catalogService;

    @Autowired
    public CallbackQueryHandler(@Lazy AbsSender absSender, CategoryService categoryService, CatalogService catalogService) {
        this.absSender = absSender;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
    }

    public void handleCallbackQuery(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();

        if (data.startsWith("CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);
            String text = "Выберите подкатегорию:";
            catalogService.sendCategories(chatId, subcategories, text, categoryService.findParentIdByCategoryId(categoryId));
        } else if (data.startsWith("BACK_TO_CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[3]);
            if (categoryId == 0) {
                catalogService.sendCatalogAsButtons(chatId);
            } else {
                List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);
                String text = "Выберите подкатегорию:";
                catalogService.sendCategories(chatId, subcategories, text, categoryService.findParentIdByCategoryId(categoryId));
            }
        }
    }
}
