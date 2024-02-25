package tg.bot.handlers.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.User;
import tg.bot.model.enums.CommandBot;
import tg.bot.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StartCommand implements UpdateHandler {
    private static final Logger log = LoggerFactory.getLogger(StartCommand.class);

    private final UserRepository userRepository;
    private final AbsSender absSender;

    @Autowired
    public StartCommand(UserRepository userRepository, @Lazy AbsSender absSender) {
        this.userRepository = userRepository;
        this.absSender = absSender;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.START;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            if (update.getMessage().hasContact()) {
                handleContact(chatId, update.getMessage().getContact());
            } else if (update.getMessage().hasText()) {
                handleStart(chatId, update.getMessage().getText(), userName);
            }
        }
    }

    private void handleContact(long chatId, Contact contact) {
        String phoneNumber = contact.getPhoneNumber();
        Optional<User> existingUser = userRepository.findByChatId(chatId);
        existingUser.ifPresent(user -> {
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
            sendMessage(chatId, "Спасибо, ваш номер телефона сохранен.");
        });
    }

    private void handleStart(long chatId, String text, String userName) {
        if (text.equals("/start")) {
            Optional<User> existingUser = userRepository.findByChatId(chatId);
            if (!existingUser.isPresent()) {
                requestPhoneNumber(chatId);
            } else {
                sendMessage(chatId, "Вы уже зарегистрированы.");
            }
        }
    }

    private void requestPhoneNumber(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Пожалуйста, отправьте ваш номер телефона.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton button = new KeyboardButton("Отправить мой номер телефона");
        button.setRequestContact(true);
        row.add(button);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true); // Убедитесь, что клавиатура скрывается после использования
        keyboardMarkup.setResizeKeyboard(true); // Подгонка размера клавиатуры под количество элементов

        message.setReplyMarkup(keyboardMarkup);

        sendMessage(message);
    }

    private void sendMessage(SendMessage message) {
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: ", e);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        sendMessage(message);
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().startsWith("/start");
    }
}