package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.model.Client;
import tg.bot.model.enums.BotState;
import tg.bot.repository.ClientRepository;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository userRepository;
    @Autowired
    public ClientService(ClientRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUserIfNotExists(long chatId, String tgName, String phoneNumber) {
        if (!userRepository.existsByChatId(chatId)) {
            Client newUser = new Client();
            newUser.setChatId(chatId);
            newUser.setTgName(tgName);
            newUser.setPhoneNumber(phoneNumber);
            userRepository.save(newUser);
        }
    }
    public Optional<Client> findByChatId(long chatId) {
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
