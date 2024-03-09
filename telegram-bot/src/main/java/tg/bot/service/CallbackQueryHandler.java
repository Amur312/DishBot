package tg.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.model.Category;
import tg.bot.model.Product;

import java.util.List;

@Slf4j
@Component
public class CallbackQueryHandler {
    private final AbsSender absSender;
    private final CategoryService categoryService;
    private final CatalogService catalogService;
    private final ProductService productService;
    @Autowired
    public CallbackQueryHandler(@Lazy AbsSender absSender, CategoryService categoryService, CatalogService catalogService, @Lazy  ProductService productService) {
        this.absSender = absSender;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        this.productService = productService;
    }

    public void handleCallbackQuery(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();

        if (data.startsWith("CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);
            if (subcategories.isEmpty()) {
                List<Product> products = productService.findProductsByCategoryId(categoryId);
                productService.sendProductsAsButtons(chatId, products);
            } else {
                String text = "Выберите подкатегорию:";
                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                Long parentId = categoryService.findParentIdByCategoryId(categoryId);
                catalogService.updateMessageWithCategories(chatId, messageId, subcategories, text, parentId);
            }
        } else if (data.startsWith("PRODUCT_")) {
            Long productId = Long.parseLong(data.split("_")[1]);
            Product product = productService.findProductById(productId);
            if (product != null) {
                productService.sendProductDetails(chatId, product);
            } else {
                log.error("Product with id " + productId + " not found.");
                // Отправляем сообщение об ошибке, если продукт не найден
            }
        } else if (data.startsWith("BACK_TO_CATEGORY_")) {
            Long backToCategoryId = Long.parseLong(data.split("_")[3]);
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (backToCategoryId == 0) {
                List<Category> categories = categoryService.findAllRootCategories();
                catalogService.updateMessageWithCategories(chatId, messageId, categories, "Выберите категорию:", null);
            } else {
                List<Category> subcategories = categoryService.findSubcategoriesByParentId(backToCategoryId);
                Long parentId = categoryService.findParentIdByCategoryId(backToCategoryId);
                catalogService.updateMessageWithCategories(chatId, messageId, subcategories, "Выберите подкатегорию:", parentId);
            }
        }
    }
}
