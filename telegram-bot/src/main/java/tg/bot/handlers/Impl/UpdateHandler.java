package tg.bot.handlers.Impl;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для обработчиков обновлений в боте.
 * Расширяет интерфейс Handler.
 */
public interface UpdateHandler extends Handler {

    /**
     * Обрабатывает обновление.
     *
     * @param update объект Update, представляющий пришедшее обновление
     */
    void handleUpdate(Update update);

    /**
     * Проверяет, может ли данный обработчик обработать указанное обновление.
     *
     * @param update объект Update, представляющий пришедшее обновление
     * @return true, если обработчик может обработать обновление, иначе false
     */
    boolean canHandleUpdate(Update update);
}
