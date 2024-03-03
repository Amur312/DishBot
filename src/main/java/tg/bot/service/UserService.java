package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.model.User;
import tg.bot.model.enums.BotState;
import tg.bot.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUserIfNotExists(long chatId, String tgName, String phoneNumber) {
        if (!userRepository.existsByChatId(chatId)) {
            User newUser = new User();
            newUser.setChatId(chatId);
            newUser.setTgName(tgName);
            newUser.setPhoneNumber(phoneNumber);
            userRepository.save(newUser);
        }
    }
    public Optional<User> findByChatId(long chatId) {
        return userRepository.findByChatId(chatId);
    }
    public void updateFirstName(long chatId, String firstName) {
        userRepository.findByChatId(chatId).ifPresent(user -> {
            user.setFirstName(firstName);
            userRepository.save(user);
        });
    }
    public void updateLastName(long chatId, String lastName) {
        userRepository.findByChatId(chatId).ifPresent(user -> {
            user.setLastName(lastName);
            userRepository.save(user);
        });
    }
    public void updateUserState(long chatId, BotState newState) {
        userRepository.findByChatId(chatId).ifPresent(user -> {
            user.setState(newState);
            userRepository.save(user);
        });
    }
}
