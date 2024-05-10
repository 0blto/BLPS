package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.Cart;
import com.drainshawty.lab1.model.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    ProductService productService;
    UserService userService;
    @Autowired
    public CartService(ProductService productService, UserService userService) {
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public Optional<HashMap<Product, Long>> getUserCart(Long id) {
        return userService.get(id).map(u ->
                u.getCart().stream().collect(HashMap::new, (m, v) ->
                                m.put(v.getProduct(), v.getQuantity()), HashMap::putAll)
        );
    }

    @Transactional
    public Optional<Long> addProduct(Long userId, Long productId) {
        return userService.get(userId).map(u -> {
            Optional<Cart> needed = u.getCart()
                    .stream()
                    .filter(c -> Objects.equals(c.getProduct().getId(), productId))
                    .findFirst();
            if (needed.isPresent()) {
                Cart c = needed.get();
                c.setQuantity(c.getQuantity() + 1);
                userService.save(u);
                return c.getQuantity();
            }
            Optional<Product> product = productService.get(productId);
            if (product.isPresent()) {
                u.getCart().add(Cart.builder().quantity(1L).product(product.get()).customer(u).build());
                userService.save(u);
                return 1L;
            }
            return 0L;
        });
    }
}
