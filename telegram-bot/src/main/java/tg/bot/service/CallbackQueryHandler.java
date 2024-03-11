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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CallbackQueryHandler {
    private final AbsSender absSender;
    private final CategoryService categoryService;
    private final CatalogService catalogService;
    private final ProductService productService;
    private final int PRODUCTS_PER_PAGE = 10;
    AtomicInteger currentPage = new AtomicInteger();
    @Autowired
    public CallbackQueryHandler(@Lazy AbsSender absSender, CategoryService categoryService,
                                CatalogService catalogService, ProductService productService) {
        this.absSender = absSender;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        this.productService = productService;
    }

    public void handleCallbackQuery(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        if (data.startsWith("CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);
            if (subcategories.isEmpty()) {
                List<Product> products = productService.findProductsByCategoryId(categoryId);
                productService.sendProductsAsButtons(chatId, products, 0, categoryId, messageId);

            } else {
                String text = "Выберите подкатегорию:";
                Long parentId = categoryService.findParentIdByCategoryId(categoryId);
                catalogService.updateMessageWithCategories(chatId, messageId, subcategories, text, parentId);
            }
        } else if (data.startsWith("PRODUCT_")) {

            Long productId = Long.parseLong(data.split("_")[1]);
            Product product = productService.findProductById(productId);
            if (product != null) {
                productService.sendProductDetails(chatId, product);
            } else {
                System.out.println("Product with id " + productId + " not found.");
            }
        } else if (data.startsWith("BACK_TO_CATEGORY_")) {
            Long backToCategoryId = Long.parseLong(data.split("_")[3]);
            if (backToCategoryId == 0) {
                List<Category> categories = categoryService.findAllRootCategories();
                catalogService.updateMessageWithCategories(chatId, messageId, categories, "Выберите категорию:", null);
            } else {
                List<Category> subcategories = categoryService.findSubcategoriesByParentId(backToCategoryId);
                Long parentId = categoryService.findParentIdByCategoryId(backToCategoryId);
                catalogService.updateMessageWithCategories(chatId, messageId, subcategories, "Выберите подкатегорию:", parentId);
            }
        } else if (data.startsWith("PAGE_")) {
            String[] parts = data.split("_");
            currentPage.set(Integer.parseInt(parts[1]));
            Long categoryId = Long.parseLong(parts[2]);
            List<Product> products = productService.findProductsByCategoryId(categoryId);
            int totalPageCount = (int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE);
            if (currentPage.get() >= 0 && currentPage.get() < totalPageCount){
                int updatedPage = productService.sendProductsAsButtons(chatId, products, currentPage.get(), categoryId, messageId);
                System.out.println("updatedPage -------> " + updatedPage);
                currentPage.set(updatedPage);
            } else {
                log.error("Requested page " + currentPage + " is out of bounds.");
            }
        }
    }
}
