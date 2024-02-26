package tg.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.User;
import tg.bot.model.enums.CommandBot;
import tg.bot.repository.UserRepository;
import tg.bot.service.UserService;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class CommandDispatcher {
    private final Map<CommandBot, UpdateHandler> handlers = new HashMap<>();
    private final UserService userService; // Используем UserService для управления пользователями

    @Autowired
    public CommandDispatcher(UserService userService) {
        this.userService = userService;
    }

    public void registerHandler(CommandBot command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void dispatch(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasContact()) {
                Contact contact = message.getContact();
                // Вызываем метод createUserIfNotExists из UserService
                userService.createUserIfNotExists(message.getChatId(), message.getFrom().getUserName(), contact.getPhoneNumber());
            } else if (message.hasText()) {
                handleTextMessage(update, message);
            }
        }
    }

    private void handleTextMessage(Update update, Message message) {
        String commandText = message.getText().split(" ")[0];
        if (commandText.startsWith("/")) {
            commandText = commandText.substring(1).toUpperCase();
            try {
                CommandBot command = CommandBot.valueOf(commandText);
                UpdateHandler handler = handlers.get(command);
                if (handler != null && handler.canHandleUpdate(update)) {
                    handler.handleUpdate(update);
                }
            } catch (IllegalArgumentException e) {
                log.warn("Неизвестная команда: {}", commandText);
            }
        }
    }
}

