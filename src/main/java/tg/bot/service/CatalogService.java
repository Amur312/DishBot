package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
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

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите категорию:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Category category : categories) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(category.getName());
            btn.setCallbackData("CATEGORY_" + category.getId());

            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        sendMessage(absSender, message);
    }
}
