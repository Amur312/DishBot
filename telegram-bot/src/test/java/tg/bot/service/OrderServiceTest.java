package tg.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tg.bot.model.Client;
import tg.bot.model.Order;
import tg.bot.model.Product;
import tg.bot.model.enums.OrderStatus;
import tg.bot.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindOrCreateOrderForClient_ExistingOrder() {

        Client client = new Client();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(50));


        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setClient(client);
        existingOrder.setStatus(OrderStatus.WAITING);


        when(orderRepository.findByClientAndStatus(client, OrderStatus.WAITING)).thenReturn(Optional.of(existingOrder));

        Order result = orderService.findOrCreateOrderForClient(client, product);


        assertEquals(existingOrder, result);


        verify(orderRepository, never()).save(any());
    }






//    @Test
//    void testFindOrCreateOrderForClient_NewOrder() {
//
//        Client client = new Client();
//        client.setId(1L);
//
//        Product product = new Product();
//        product.setId(1L);
//        product.setPrice(BigDecimal.valueOf(50));
//
//
//        when(orderRepository.findByClientAndStatus(client, OrderStatus.WAITING)).thenReturn(Optional.empty());
//
//
//        Order result = orderService.findOrCreateOrderForClient(client, product);
//        System.out.println(result);
//
//        assertNotNull(result);
//        assertEquals(client, result.getClient());
//        assertEquals(OrderStatus.WAITING, result.getStatus());
//        assertEquals(product.getPrice(), result.getAmount());
//        assertNotNull(result.getCreatedDate());
//
//
//        verify(orderRepository, times(1)).save(any());
//    }

}
