package tg.bot.handlers.commands;

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

@Component
public class StartCommand implements UpdateHandler {
    private UserRepository userRepository;
    private AbsSender absSender;

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
        long chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText().toLowerCase();
            if (command.equals("/start")) {
                if (!userRepository.existsByChatId(chatId)) {
                    requestContact(chatId);
                } else {
                    sendMessage(chatId, "Вы уже зарегистрированы.");
                }
            }
        } else if (update.hasMessage() && update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            String phoneNumber = contact.getPhoneNumber();
            if (!userRepository.existsByPhoneNumber(phoneNumber)) {
                User user = new User(update.getMessage().getFrom().getUserName(), chatId, phoneNumber);
            }
        }

    }

    private void requestContact(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();


        KeyboardButton contactButton = new KeyboardButton("Поделиться номером");
        contactButton.setRequestContact(true);
        row.add(contactButton);
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        sendMessage(chatId, "Привет! Пожалуйста, поделитесь своим номером, нажав на кнопку ниже, чтобы начать работу с ботом.", replyKeyboardMarkup);
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        String command = update.getMessage().getText().toLowerCase();
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().startsWith("/start");
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup replyMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(replyMarkup);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }
}
