package com.drainshawty.lab1.model.shoppingdb;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPK implements Serializable {
    @Column(name = "order_id")
    Long orderId;

    @Column(name = "customer_id")
    Long customerId;

    @Column(name = "product_id")
    Long productId;
}
