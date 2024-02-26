package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.model.User;
import tg.bot.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUserIfNotExists(long chatId, String userName, String phoneNumber) {
        if (!userRepository.existsByChatId(chatId)) {
            User newUser = new User();
            newUser.setChatId(chatId);
            newUser.setUserName(userName);
            newUser.setPhoneNumber(phoneNumber);
            userRepository.save(newUser);
        }
    }
}
