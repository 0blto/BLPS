package com.drainshawty.lab1.http.responces;

import com.drainshawty.lab1.model.shoppingdb.Cart;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class CartResp implements Serializable {

    @Builder.Default public String msg = "";
    @Builder.Default public List<Cart> cart = Collections.emptyList();
}
