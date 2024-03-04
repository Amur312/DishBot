package tg.bot.handlers.Impl;

import tg.bot.model.enums.CommandBot;

/**
 * Интерфейс для обработчиков команд бота.
 */
public interface Handler {

    /**
     * Возвращает команду, обрабатываемую данным обработчиком.
     *
     * @return команда бота, которую обрабатывает данный обработчик
     */
    CommandBot getCommand();
}