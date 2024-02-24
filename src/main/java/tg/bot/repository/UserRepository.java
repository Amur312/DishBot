package tg.bot.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;
import tg.bot.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);

    Optional<User> findByUserName(String userName);

    List<User> findAll();

    User save(User user);

    boolean existsByChatId(Long chatId);
    boolean existsByPhoneNumber(String phoneNumber);
}
