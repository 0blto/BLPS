package com.drainshawty.lab1.http.requests;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReq {
    String name;
    String description;
    @PositiveOrZero
    Long number;
    @Positive
    Long price;

}
