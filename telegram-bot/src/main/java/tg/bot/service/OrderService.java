package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.model.*;
import tg.bot.model.enums.OrderStatus;
import tg.bot.repository.OrderRepository;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final ClientService clientService;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository repository, ClientService clientService, ProductService productService) {
        this.repository = repository;
        this.clientService = clientService;
        this.productService = productService;
    }

    @Transactional
    public void addToCart(Long chatId, Long productId) {
        Client client = clientService.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        Product product = productService.findProductById(productId);
        Order order = findOrCreateOrderForClient(client, product);
        addProductToOrder(order, product);
        repository.save(order);
    }

    private Order findOrCreateOrderForClient(Client client, Product product) {
        Optional<Order> existingOrder = repository.findByClientAndStatus(client, OrderStatus.WAITING);
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        } else {
            Order newOrder = new Order();
            newOrder.setClient(client);
            newOrder.setStatus(OrderStatus.WAITING);
            newOrder.setAmount(product.getPrice());
            newOrder.setCreatedDate(java.time.LocalDateTime.now());

            return repository.save(newOrder);
        }
    }

    private void addProductToOrder(Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductName(product.getName());
        orderItem.setProductPrice(product.getPrice());

        order.getItems().add(orderItem);
    }
}
