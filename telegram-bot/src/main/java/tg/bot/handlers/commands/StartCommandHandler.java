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
import tg.bot.model.Client;
import tg.bot.model.enums.CommandBot;
import tg.bot.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class StartCommandHandler implements UpdateHandler {
    private final ClientRepository userRepository;
    private final AbsSender absSender;
    private final MainMenuService mainMenuService;
    private MessageUtils messageUtils;
    @Autowired
    public StartCommandHandler(ClientRepository userRepository, @Lazy AbsSender absSender, MainMenuService mainMenuService) {
        this.userRepository = userRepository;
        this.absSender = absSender;
        this.mainMenuService = mainMenuService;
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
        }
    }

    private void handleStart(long chatId, String text) {
        if ("/start".equals(text)) {
            Optional<Client> existingUser = userRepository.findByChatId(chatId);
            if (!existingUser.isPresent()) {
                requestPhoneNumber(chatId);
            } else {
                mainMenuService.sendMainMenu(chatId);
            }
        }
    }


    private void requestPhoneNumber(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Привет! Пожалуйста, поделитесь своим номером, нажав на кнопочку ниже, чтобы начать работу с ботом.");

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

        message.setReplyMarkup(keyboardMarkup);

        messageUtils.sendMessage(absSender,message);
    }



    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().startsWith("/start");
    }
}