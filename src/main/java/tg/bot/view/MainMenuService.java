package tg.bot.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.util.MessageUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuService {
    private final AbsSender absSender;
    private Logger logger = LoggerFactory.getLogger(MainMenuService.class);

    @Autowired
    public MainMenuService(@Lazy AbsSender absSender) {
        this.absSender = absSender;
    }

    public void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите опцию:");

        ReplyKeyboardMarkup keyboardMarkup = createMainMenuKeyboard();
        message.setReplyMarkup(keyboardMarkup);

        MessageUtils.sendMessage(absSender, message);
    }

    private ReplyKeyboardMarkup createMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Каталог");
        row1.add("Корзина");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Поиск");
        row2.add("История Заказов");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Контакты");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}

