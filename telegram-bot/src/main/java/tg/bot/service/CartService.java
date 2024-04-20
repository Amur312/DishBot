package tg.bot.service;

import org.springframework.stereotype.Service;
import tg.bot.model.Order;
import tg.bot.model.OrderItem;
import tg.bot.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static tg.bot.util.MessageUtils.sendMessage;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    public CartService(OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    public List<OrderItem> getCartItems(Long chatId) {
        List<Order> orderOptional = orderRepository.findByClientChatId(chatId);
        if(!orderOptional.isEmpty()) {
            Order order = orderOptional.get(0);
            return order.getItems();
        }else {
            return List.of();
        }
    }
}
