package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.model.*;
import tg.bot.model.enums.OrderStatus;
import tg.bot.repository.OrderRepository;

import java.util.List;
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
        Long cleintId = client.getId();
        int quantity = 1;
        addProductToOrder(order, product, cleintId, quantity);
        repository.save(order);
    }

    public Order findOrCreateOrderForClient(Client client, Product product) {
        List<Order> existingOrder = repository.findByClientAndStatus(client, OrderStatus.WAITING);
        if (!existingOrder.isEmpty()) {
            return existingOrder.get(0);
        } else {
            Order newOrder = new Order();
            newOrder.setClient(client);
            newOrder.setStatus(OrderStatus.WAITING);
            newOrder.setAmount(product.getPrice());
            newOrder.setCreatedDate(java.time.LocalDateTime.now());
            return repository.save(newOrder);
        }
    }

    private void addProductToOrder(Order order, Product product, Long cleintId, int quantity) {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductName(product.getName());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setClientId(cleintId);
        orderItem.setQuantity(quantity);

        order.getItems().add(orderItem);
    }

}
