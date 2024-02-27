package tg.bot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageUtils {
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * Отправляет текстовое сообщение пользователю.
     *
     * @param absSender экземпляр AbsSender для отправки сообщения через Telegram API
     * @param chatId ID чата для отправки сообщения
     * @param text текст сообщения
     */
    public static void sendMessage(AbsSender absSender, long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: ", e);
            sendErrorMessage(absSender, chatId);
        }
    }

    /**
     * Отправляет сообщение об ошибке пользователю в случае возникновения исключения при отправке основного сообщения.
     *
     * @param absSender экземпляр AbsSender для отправки сообщения через Telegram API
     * @param chatId ID чата для отправки сообщения об ошибке
     */
    private static void sendErrorMessage(AbsSender absSender, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Извините, произошла ошибка. Пожалуйста, попробуйте позже.");
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение об ошибке: ", e);
        }
    }

    /**
     * Отправляет сообщение с использованием объекта SendMessage, позволяя настроить дополнительные параметры сообщения,
     * такие как клавиатура.
     *
     * @param absSender экземпляр AbsSender для отправки сообщения через Telegram API
     * @param message объект SendMessage с настроенными параметрами сообщения
     */
    public static void sendMessage(AbsSender absSender, SendMessage message) {
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: ", e);
            sendErrorMessage(absSender, Long.parseLong(message.getChatId()));
        }
    }

}
