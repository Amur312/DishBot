package tg.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.IMessageHandler;
import tg.bot.model.enums.BotState;
import tg.bot.service.UserService;

import static tg.bot.util.MessageUtils.sendMessage;

@Component
public class ContactMessageHandler implements IMessageHandler {
    private final UserService userService;
    private final AbsSender absSender;

    @Autowired
    public ContactMessageHandler(UserService userService, @Lazy AbsSender absSender) {
        this.userService = userService;
        this.absSender = absSender;
    }

    @Override
    public void handle(Message message) {
        userService.createUserIfNotExists(message.getChatId(), message.getFrom().getUserName(), message.getContact().getPhoneNumber());
        userService.updateUserState(message.getChatId(), BotState.AWAITING_FIRST_NAME);
        sendMessage(absSender, message.getChatId(), "Пожалуйста, введите ваше имя.");
    }

}
