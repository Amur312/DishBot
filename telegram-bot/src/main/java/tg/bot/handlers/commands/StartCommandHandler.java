package tg.bot.handlers.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.util.MessageUtils;
import tg.bot.view.MainMenuService;

import tg.bot.model.enums.CommandBot;
import tg.bot.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class StartCommandHandler implements UpdateHandler {
    private final ClientRepository userRepository;
    private final AbsSender absSender;
    private final MainMenuService mainMenuService;
    private final MessageUtils messageUtils;

    public StartCommandHandler(ClientRepository userRepository, @Lazy AbsSender absSender, MainMenuService mainMenuService, MessageUtils messageUtils) {
        this.userRepository = userRepository;
        this.absSender = absSender;
        this.mainMenuService = mainMenuService;
        this.messageUtils = messageUtils;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.START;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            handleStart(chatId, update.getMessage().getText());
        } else {
            log.warn("Получено обновление без сообщения для команды /start");
        }
    }

    private void handleStart(long chatId, String text) {
        if ("/start".equals(text)) {
            userRepository.findByChatId(chatId).ifPresentOrElse(
                    client -> mainMenuService.sendMainMenu(chatId),
                    () -> requestPhoneNumber(chatId)
            );
        } else {
            log.warn("Получено обновление с текстом, отличным от /start: {}", text);
        }
    }

    private void requestPhoneNumber(long chatId) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Привет! Пожалуйста, поделитесь своим номером, нажав на кнопочку ниже, чтобы начать работу с ботом.");
            message.setReplyMarkup(createReplyKeyboardMarkup());

            MessageUtils.sendMessage(absSender, message);
            log.info("Запрос номера телефона отправлен в чат {}", chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса номера телефона в чат {}", chatId, e);
        }
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton button = new KeyboardButton("Отправить мой номер телефона");
        button.setRequestContact(true);
        row.add(button);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            return text.startsWith("/start");
        }
        log.warn("Обновление не содержит сообщения или текста для команды /start");
        return false;
    }
}
