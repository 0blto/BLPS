package com.drainshawty.lab1.model.shoppingdb;


import com.drainshawty.lab1.serializers.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")}
)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = ProductSerializer.class)
public class Product {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @PositiveOrZero
    Long number;

    @Positive
    Long price;
}
