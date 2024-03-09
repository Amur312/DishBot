package com.site.admin.models.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(value = 1, message = "Minimum price is 1 $")
    private BigDecimal price;

    @Lob
    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    public Product() {
    }

}
