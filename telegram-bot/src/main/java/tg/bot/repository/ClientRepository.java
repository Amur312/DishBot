package tg.bot.repository;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;
import tg.bot.model.Client;

import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan("tg.bot.repository")
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByChatId(Long chatId);

    List<Client> findAll();

    Client save(Client user);

    boolean existsByChatId(Long chatId);
    boolean existsByPhoneNumber(String phoneNumber);
}
