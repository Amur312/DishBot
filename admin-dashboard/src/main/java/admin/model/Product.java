package admin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_product", nullable = false)
    private String nameProduct;
    @Column(name = "description")
    private String description;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Lob
    @Column(name = "image")
    private byte[] image;

    public Product() {
    }

    public Product(String nameProduct, String description, BigDecimal price) {
        this.nameProduct = nameProduct;
        this.description = description;
        this.price = price;
    }
}
