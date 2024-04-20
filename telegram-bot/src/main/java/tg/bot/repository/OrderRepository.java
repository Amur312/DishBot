package tg.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.model.Client;
import tg.bot.model.Order;
import tg.bot.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByClientAndStatus(Client client, OrderStatus status);
    List<Order> findByClientChatId(Long chatId);
}
