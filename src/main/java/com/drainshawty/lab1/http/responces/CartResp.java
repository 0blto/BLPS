package com.drainshawty.lab1.http.responces;

import com.drainshawty.lab1.model.Cart;
import com.drainshawty.lab1.model.Product;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class CartResp implements Serializable {

    @Builder.Default public String msg = "";
    @Builder.Default public List<Cart> cart = Collections.emptyList();
}
