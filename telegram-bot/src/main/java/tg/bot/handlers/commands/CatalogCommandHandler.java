package tg.bot.handlers.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CatalogService;
import tg.bot.util.ConvertEmojiToCommand;


@Component
public class CatalogCommandHandler implements UpdateHandler {
    private final AbsSender absSender;
    private final CatalogService catalogService;
    private final ConvertEmojiToCommand utilEmoji;

    @Autowired
    public CatalogCommandHandler(@Lazy AbsSender absSender, CatalogService catalogService, ConvertEmojiToCommand utilEmoji) {
        this.absSender = absSender;
        this.catalogService = catalogService;
        this.utilEmoji = utilEmoji;
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.CATALOG;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            catalogService.sendCatalogAsButtons(chatId);
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return update.hasMessage() && update.getMessage().hasText() &&
                (convertedCommand.equalsIgnoreCase("/catalog"));
    }
}
