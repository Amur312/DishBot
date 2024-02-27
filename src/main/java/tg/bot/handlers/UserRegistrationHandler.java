package tg.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.IMessageHandler;
import tg.bot.model.User;
import tg.bot.model.enums.BotState;
import tg.bot.service.UserService;

import static tg.bot.util.MessageUtils.sendMessage;
@Slf4j
@Component
public class UserRegistrationHandler implements IMessageHandler {
    private final UserService userService;
    private final AbsSender absSender;

    @Autowired
    public UserRegistrationHandler(UserService userService, @Lazy AbsSender absSender) {
        this.userService = userService;
        this.absSender = absSender;
    }

    @Override
    public void handle(Message message) {
        User user = userService.findByChatId(message.getChatId()).orElse(null);
        if (user != null) {
            switch (user.getState()) {
                case AWAITING_FIRST_NAME:
                    String firstName = message.getText();
                    userService.updateFirstName(message.getChatId(), firstName);
                    userService.updateUserState(message.getChatId(), BotState.AWAITING_LAST_NAME);
                    sendMessage(absSender, message.getChatId(), "Пожалуйста, введите вашу фамилию.");
                    break;
                case AWAITING_LAST_NAME:
                    String lastName = message.getText();
                    userService.updateLastName(message.getChatId(), lastName);
                    userService.updateUserState(message.getChatId(), BotState.NONE);
                    sendMessage(absSender, message.getChatId(), "Спасибо, " + user.getFirstName() + ", ваша регистрация завершена.");
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("Обработка!!!!!");
        }
    }
}
