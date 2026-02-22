package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;

    @ManyToOne
    private Category category;
}
