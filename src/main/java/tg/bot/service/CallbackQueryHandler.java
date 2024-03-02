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
    @Autowired
    public CallbackQueryHandler(@Lazy AbsSender absSender, CategoryService categoryService) {
        this.absSender = absSender;
        this.categoryService = categoryService;
    }

    public void handleCallbackQuery(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();

        if (data.startsWith("CATEGORY_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            sendSubcategoriesAsButtons(chatId, categoryId);
        }
    }

    private void sendSubcategoriesAsButtons(long chatId, Long categoryId) {
        List<Category> subcategories = categoryService.findSubcategoriesByParentId(categoryId);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите подкатегорию:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Category subcategory : subcategories) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(subcategory.getName());
            btn.setCallbackData("SUBCATEGORY_" + subcategory.getId());

            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        sendMessage(absSender, message);
    }
}
