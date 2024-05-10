package com.drainshawty.lab1.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")}
)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
