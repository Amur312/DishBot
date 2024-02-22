package tg.bot.bot;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.config.BotConfig;
import tg.bot.model.User;
import tg.bot.repository.UserRepository;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private UserRepository userRepository;
    private BotConfig botConfig;

    public TelegramBot(BotConfig botConfig, UserRepository userRepository) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String massageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (massageText) {
                case "/start":
                    if (!userRepository.existsByChatId(chatId)) {
                        User user = new User(update.getMessage().getFrom().getUserName(), chatId);
                        userRepository.save(user);
                    }
                    break;
                default:
                    sendMessage(chatId, "Sorry");
            }
        }
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.getMessage();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
