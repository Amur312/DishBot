package tg.bot.handlers.Impl;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface IMessageHandler {
    void handle(Message message);
}
