package com.drainshawty.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @EmbeddedId
    CartPK cartPK;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    User customer;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Product product;

    @Positive
    Long quantity;
}
