package com.drainshawty.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public enum Status {
        CREATED,
        SHIPPING,
        ARRIVED,

        RECEIVED,

        RETURNED

    }

    @EmbeddedId
    OrderPK orderPK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Product product;

    @Positive
    Long quantity;

    @NotNull
    Status status;


}
