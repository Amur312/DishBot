package tg.bot.handlers.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;
import tg.bot.util.ConvertEmojiToCommand;
import static tg.bot.util.MessageUtils.sendMessage;

@Slf4j
@Component
public class ContactsCommandHandler implements UpdateHandler {
    private final AbsSender absSender;
    private final ConvertEmojiToCommand utilEmoji;

    @Autowired
    public ContactsCommandHandler(@Lazy AbsSender absSender, ConvertEmojiToCommand utilEmoji) {
        this.absSender = absSender;
        this.utilEmoji = utilEmoji;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.CONTACTS;
    }

    @Override
    public void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String contactInfo = "Вот наши контактные данные: +7-909-444-44-44 TG:@Amur312";
        sendMessage(absSender, chatId, contactInfo);
    }


    @Override
    public boolean canHandleUpdate(Update update) {
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return update.hasMessage() && update.getMessage().hasText() &&
                (convertedCommand.equalsIgnoreCase("/contacts"));

    }
}
