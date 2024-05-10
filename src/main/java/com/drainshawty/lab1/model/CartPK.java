package com.drainshawty.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartPK implements Serializable {
    @Column(name = "customer_id")
    Long customerId;

    @Column(name = "product_id")
    Long productId;
}
