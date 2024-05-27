package com.drainshawty.lab1.services;

import com.drainshawty.lab1.exceptions.NotFoundException;
import com.drainshawty.lab1.model.shoppingdb.Cart;
import com.drainshawty.lab1.model.shoppingdb.CartPK;
import com.drainshawty.lab1.repo.shoppingdb.CartRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    ProductService productService;
    UserService userService;
    CartRepo cartRepo;
    @Autowired
    public CartService(ProductService productService, UserService userService, CartRepo cartRepo) {
        this.userService = userService;
        this.productService = productService;
        this.cartRepo = cartRepo;
    }

    @Transactional
    public Optional<List<Cart>> getUserCart(String email) {
        return Optional.ofNullable(userService.get(email)
                .map(u -> cartRepo.getByCartPK_CustomerId(u.getUserId()))
                .orElseThrow(() -> new NotFoundException("User not found!"))
        );
    }

    @Transactional
    public Optional<List<Cart>> addProduct(String email, Long productId) {
        return productService.get(productId).map(p ->
            userService.get(email).map(u -> {
                Optional<Cart> needed =
                        cartRepo.getByCartPK_CustomerId(u.getUserId())
                                .stream()
                                .filter(c ->
                                        Objects.equals(c.getProduct().getId(), p.getId())
                                ).findFirst();
                if (needed.isPresent()) {
                    needed.get().setQuantity(
                                    needed.get().getQuantity() < p.getNumber()
                                            ? needed.get().getQuantity() + 1
                                            : p.getNumber()
                            );
                    save(needed.get());
                } else {
                    save(Cart.builder().quantity(1L)
                            .product(p)
                            .cartPK(new CartPK(u.getUserId(), p.getId())).build());
                }
                return Optional.ofNullable(cartRepo.getByCartPK_CustomerId(u.getUserId()));
            }).orElseThrow(() -> new NotFoundException("User with email" + email + "not found"))
        ).orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));
    }

    @Transactional
    public Optional<List<Cart>> removeOne(String email, Long productId) {
        return productService.get(productId).map(p ->
                userService.get(email).map(u ->
                    cartRepo.getByCartPK_CustomerId(u.getUserId()).stream()
                            .filter(c -> Objects.equals(c.getProduct().getId(), productId))
                            .findFirst()
                            .map(needed -> {
                                needed.setQuantity(Math.max(needed.getQuantity() - 1, 0));
                                if (needed.getQuantity() == 0)
                                    cartRepo.deleteByCartPK(new CartPK(u.getUserId(), productId));
                                else save(needed);
                                return Optional.ofNullable(cartRepo.getByCartPK_CustomerId(u.getUserId()));
                            }).orElseThrow(() -> new NotFoundException("No products with this id in cart"))
                ).orElseThrow(() -> new NotFoundException("User with email" + email + "not found"))
        ).orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));
    }

    @Transactional
    public Optional<List<Cart>> clearCart(String email) {
        return Optional.ofNullable(userService.get(email)
                .map(u -> {
                    cartRepo.deleteByCartPK_CustomerId(u.getUserId());
                    return cartRepo.getByCartPK_CustomerId(u.getUserId());
                }).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Transactional
    public void save(Cart c) { this.cartRepo.save(c); }
}
