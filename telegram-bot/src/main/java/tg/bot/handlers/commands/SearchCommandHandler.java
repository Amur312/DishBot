package tg.bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;

public class SearchCommandHandler implements UpdateHandler {

    @Override
    public void handleUpdate(Update update) {

    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return false;
    }

    @Override
    public CommandBot getCommand() {
        return null;
    }
}
