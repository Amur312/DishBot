package tg.bot.handlers.commands;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;
import tg.bot.service.CatalogService;
import tg.bot.util.ConvertEmojiToCommand;

@Slf4j
@Component
public class CatalogCommandHandler implements UpdateHandler {


    private final CatalogService catalogService;
    private final ConvertEmojiToCommand utilEmoji;

    public CatalogCommandHandler(CatalogService catalogService, ConvertEmojiToCommand utilEmoji) {
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
            log.info("Обработка команды /catalog для чата {}", chatId);

            try {
                catalogService.sendCatalogAsButtons(chatId);
                log.info("Каталог успешно отправлен в чат {}", chatId);
            } catch (Exception e) {
                log.error("Ошибка при отправке каталога в чат {}", chatId, e);
            }
        } else {
            log.warn("Обновление не содержит сообщения с текстом для команды /catalog");
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return convertedCommand.equalsIgnoreCase("/catalog");
    }
}
