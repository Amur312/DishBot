package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.model.Product;
import tg.bot.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

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


    public int sendProductsAsButtons(Long chatId, List<Product> products, int currentPage, Long categoryId, Integer messageId) {
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

        if (messageId == null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Выберите товар:");
            message.setReplyMarkup(markupInline);
            try {
                absSender.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error sending message", e);
            }
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId.toString());
            editMessageText.setMessageId(messageId);
            editMessageText.setText("Выберите товар:");
            editMessageText.setReplyMarkup(markupInline);

            try {
                absSender.execute(editMessageText);
            } catch (TelegramApiException e) {
                log.error("Error updating message", e);
            }
        }
        return currentPage;
    }


    private List<InlineKeyboardButton> getPaginationRow(Long chatId, int currentPage, int totalPages, Long categoryId) {
        List<InlineKeyboardButton> paginationRow = new ArrayList<>();


        BiConsumer<String, String> addButton = (text, callbackData) -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(callbackData);
            paginationRow.add(button);
        };

        if (currentPage > 0) {
            addButton.accept("⬅️ Назад", "PAGE_" + (currentPage - 1) + "_" + categoryId);
        }
        if (currentPage < totalPages - 1) {
            addButton.accept("Вперёд ➡️", "PAGE_" + (currentPage + 1) + "_" + categoryId);
        }
        if (currentPage == 0) {
            addButton.accept("Назад к категориям", "BACK_TO_CATEGORY_" + categoryId);
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
