package tg.bot.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tg.bot.model.Category;
import tg.bot.model.Product;
import tg.bot.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    private List<Product> getProducts(){
        Product product1 = new Product();
        Product product2 = new Product();

        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");

        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setCategory(category);
        product2.setId(2L);
        product2.setName("Product 2");
        product1.setPrice(BigDecimal.valueOf(200));
        product1.setCategory(category);


        return Arrays.asList(product1, product2);
    }
    @Test
    public void testFindAllProducts() {

       List<Product> expectedProducts = getProducts();
        when(productRepository.findAll()).thenReturn(expectedProducts);


        List<Product> actualProducts = productService.findAllProducts();
        assertEquals(expectedProducts.size(), actualProducts.size());
        assertTrue(actualProducts.containsAll(expectedProducts));
    }

    @Test
    public void testFindByCategoryId(){
        List<Product> expectedProducts = getProducts();
        when(productRepository.findByCategoryId(1L)).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.findProductsByCategoryId(1L);

        assertEquals(expectedProducts.size(), actualProducts.size());
        assertTrue(actualProducts.containsAll(expectedProducts));
    }

    @Test
    public void testFindProductById(){
        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setName("Test Product");
        expectedProduct.setPrice(BigDecimal.valueOf(100));


        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.findProductById(1L);

        assertEquals(expectedProduct,actualProduct);
    }
}
