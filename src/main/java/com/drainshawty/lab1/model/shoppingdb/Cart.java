package com.drainshawty.lab1.model.shoppingdb;

import com.drainshawty.lab1.model.userdb.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @EmbeddedId
    CartPK cartPK;

    @Transient
    User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Product product;

    @Positive
    Long quantity;
}
