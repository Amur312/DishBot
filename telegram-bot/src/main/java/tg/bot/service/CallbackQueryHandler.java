package tg.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.model.Category;

import java.util.List;

@Slf4j
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
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        String data = update.getCallbackQuery().getData();
        log.info("Data-> handleCallbackQuery = " + data);
        if (data.startsWith("CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            log.info("CategoryId -> handleCallbackQuery = " + categoryId);
            List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);
            String text = "Выберите подкатегорию:";
            catalogService.updateMessageWithCategories(chatId, messageId, subcategories, text, categoryService.findParentIdByCategoryId(categoryId));
        } else if (data.startsWith("BACK_TO_CATEGORY_")) {
            Long backToCategoryId = Long.parseLong(data.split("_")[3]);
            if (backToCategoryId == 0) {
                List<Category> categories = categoryService.findAllRootCategories();
                catalogService.updateMessageWithCategories(chatId, messageId, categories, "Выберите категорию:", null);
            } else {
                List<Category> subcategories = categoryService.findSubcategoriesByParentId(backToCategoryId);
                catalogService.updateMessageWithCategories(chatId, messageId, subcategories, "Выберите подкатегорию:", categoryService.findParentIdByCategoryId(backToCategoryId));
            }
        }
    }
}
