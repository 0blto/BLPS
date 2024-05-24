package com.drainshawty.lab1.model.shoppingdb;

import com.drainshawty.lab1.model.userdb.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

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
        PAID,
        SHIPPING,
        ARRIVED,

        RECEIVED,

        RETURNED

    }

    @EmbeddedId
    OrderPK orderPK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Product product;

    @Positive
    Long quantity;

    @NotNull
    Status status;


}
