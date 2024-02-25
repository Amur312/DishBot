package tg.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.User;
import tg.bot.model.enums.CommandBot;
import tg.bot.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandDispatcher {
    private final Map<CommandBot, UpdateHandler> handlers = new HashMap<>();
    private final UserRepository userRepository;

    @Autowired
    public CommandDispatcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerHandler(CommandBot command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void dispatch(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            if (message.hasContact()) {
                Contact contact = message.getContact();
                System.out.println("Phone number: " + contact.getPhoneNumber());
                if (!userRepository.existsByChatId(chatId)) {
                    User newUser = new User();
                    newUser.setChatId(chatId);
                    newUser.setUserName(message.getFrom().getUserName());
                    newUser.setPhoneNumber(contact.getPhoneNumber());
                    userRepository.save(newUser);
                }
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
                System.out.println("Неизвестная команда: " + commandText);
            }
        }
    }
}

