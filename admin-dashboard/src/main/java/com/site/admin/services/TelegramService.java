package com.site.admin.services;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramService {

    void sendMessage(Long chatId, String message) throws TelegramApiException;

}
