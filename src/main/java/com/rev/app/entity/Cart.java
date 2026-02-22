package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="carts")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @OneToOne
    private User user;
}
