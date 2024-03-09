package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
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
public class ProductService {
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

    public void sendProductsAsButtons(Long chatId, List<Product> products) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите товар:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Product product : products) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(product.getName() + " - " + product.getPrice().toString() + "$");
            button.setCallbackData("PRODUCT_" + product.getId().toString());
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(button);
            rowsInline.add(rowInline);
        }

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
