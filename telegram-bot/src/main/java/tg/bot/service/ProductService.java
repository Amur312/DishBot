package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.model.Product;
import tg.bot.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private static final int PRODUCTS_PER_PAGE = 10;
    private final ProductRepository productRepository;
    private final AbsSender absSender;

    public ProductService(ProductRepository productRepository, @Lazy AbsSender absSender) {
        this.productRepository = productRepository;
        this.absSender = absSender;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }


    public void sendProductsAsButtons(Long chatId, List<Product> products, int currentPage, Long categoryId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите товар:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        int totalPages = (int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE);
        currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

        int startIndex = currentPage * PRODUCTS_PER_PAGE;
        int endIndex = Math.min((currentPage + 1) * PRODUCTS_PER_PAGE, products.size());

        for (int i = startIndex; i < endIndex; i++) {
            Product product = products.get(i);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(product.getName() + " - " + product.getPrice().toString() + " UZS");
            button.setCallbackData("PRODUCT_" + product.getId().toString());
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(button);
            rowsInline.add(rowInline);
        }

        rowsInline.add(getPaginationRow(chatId, currentPage, totalPages, categoryId));

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> getPaginationRow(Long chatId, int currentPage, int totalPages, Long categoryId) {
        List<InlineKeyboardButton> paginationRow = new ArrayList<>();
        System.out.println("currentPage -------------------->" + currentPage);

        if (currentPage > 0) {
            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("⬅️ Назад");
            backButton.setCallbackData("PAGE_" + (currentPage - 1) + "_" + categoryId);
            paginationRow.add(backButton);
        }

        if (currentPage < totalPages - 1) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("Вперёд ➡️");
            nextButton.setCallbackData("PAGE_" + (currentPage + 1) + "_" + categoryId);
            paginationRow.add(nextButton);
        }
        if (currentPage == 0) {
            InlineKeyboardButton backToCategoriesButton = new InlineKeyboardButton();
            backToCategoriesButton.setText("Назад к категориям");
            backToCategoriesButton.setCallbackData("BACK_TO_CATEGORIES");
            paginationRow.add(backToCategoriesButton);
        }
        return paginationRow;
    }


    public void sendProductDetails(Long chatId, Product product) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(product.getDescription());
        System.out.println("PHOTO ----------------------> " + product.getPhotoUrl());
        if (product.getPhotoUrl() != null && !product.getPhotoUrl().isEmpty()) {
            sendProductPhoto(chatId, product);
        }

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendProductPhoto(Long chatId, Product product) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId.toString());
        sendPhotoRequest.setPhoto(new InputFile(product.getPhotoUrl()));
        sendPhotoRequest.setCaption(product.getName() + "\nЦена: " + product.getPrice() + "$");

        try {
            absSender.execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " was not found."));
    }
}
