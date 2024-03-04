package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.model.Category;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.util.MessageUtils.sendMessage;

@Service
public class CatalogService {
    private final CategoryService categoryService;
    private final AbsSender absSender;

    @Autowired
    public CatalogService(CategoryService categoryService, @Lazy AbsSender absSender) {
        this.categoryService = categoryService;
        this.absSender = absSender;
    }

    public void sendCatalogAsButtons(long chatId) {
        List<Category> categories = categoryService.findAllRootCategories();
        String headerText = "Выберите категорию:";
        sendCategories(chatId, categories, headerText, null);
    }

    void sendCategories(long chatId, List<Category> categories, String text, Long backToCategoryId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Category category : categories) {
            InlineKeyboardButton btn = createButtonForCategory(category);
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }

        if (backToCategoryId != null) {
            rowsInline.add(createBackButtonRow(backToCategoryId));
        }

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        sendMessage(absSender, message);
    }
    public void updateMessageWithCategories(long chatId, int messageId, List<Category> categories, String text, Long backToCategoryId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Category category : categories) {
            InlineKeyboardButton btn = createButtonForCategory(category);
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }

        if (backToCategoryId != null) {
            rowsInline.add(createBackButtonRow(backToCategoryId));
        }

        markupInline.setKeyboard(rowsInline);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText(text);
        editMessageText.setReplyMarkup(markupInline);

        try {
            absSender.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardButton createButtonForCategory(Category category) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(category.getName());
        button.setCallbackData("CATEGORY_" + category.getId());
        return button;
    }

    private List<InlineKeyboardButton> createBackButtonRow(Long categoryId) {
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("BACK_TO_CATEGORY_" + (categoryId == null ? "0" : categoryId));

        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);
        return backRow;
    }
}
