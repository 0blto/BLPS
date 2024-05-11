package com.drainshawty.lab1.http.requests;

import com.drainshawty.lab1.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReq {
    Long id;
    Order.Status status;
}
