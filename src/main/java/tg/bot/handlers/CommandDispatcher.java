package tg.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.enums.CommandBot;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<CommandBot, UpdateHandler> handlers = new HashMap<>();

    public void registerHandler(CommandBot command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void dispatch(Update update) {
        String commandText = update.getMessage().getText().split(" ")[0];
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
