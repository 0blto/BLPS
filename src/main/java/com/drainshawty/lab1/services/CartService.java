package com.drainshawty.lab1.services;

import com.drainshawty.lab1.exceptions.NotFoundException;
import com.drainshawty.lab1.model.Cart;
import com.drainshawty.lab1.model.CartPK;
import com.drainshawty.lab1.model.Product;
import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.repo.CartRepo;
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
                .map(cartRepo::getByCustomer)
                .orElseThrow(() -> new NotFoundException("User not found!"))
        );
    }

    @Transactional
    public Optional<List<Cart>> addProduct(String email, Long productId) {
        return productService.get(productId).map(p ->
            userService.get(email).map(u -> {
                Optional<Cart> needed = cartRepo.getByCustomer(u).stream().filter(c -> Objects.equals(c.getProduct().getId(), productId)).findFirst();
                if (needed.isPresent()) {
                    needed.get().setQuantity(needed.get().getQuantity() < p.getNumber() ? needed.get().getQuantity() + 1 : p.getNumber());
                    save(needed.get());
                } else {
                    save(Cart.builder().quantity(1L)
                            .product(p).customer(u)
                            .cartPK(new CartPK(u.getUserId(), p.getId())).build());
                }
                return Optional.ofNullable(cartRepo.getByCustomer(u));
            }).orElseThrow(() -> new NotFoundException("User with email" + email + "not found"))
        ).orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));
    }

    @Transactional
    public Optional<List<Cart>> removeOne(String email, Long productId) {
        return productService.get(productId).map(p ->
                userService.get(email).map(u ->
                    cartRepo.getByCustomer(u).stream()
                            .filter(c -> Objects.equals(c.getProduct().getId(), productId))
                            .findFirst()
                            .map(needed -> {
                                needed.setQuantity(Math.max(needed.getQuantity() - 1, 0));
                                if (needed.getQuantity() == 0)
                                    cartRepo.deleteByCartPK(new CartPK(u.getUserId(), productId));
                                else save(needed);
                                return Optional.ofNullable(cartRepo.getByCustomer(u));
                            }).orElseThrow(() -> new NotFoundException("No products with this id in cart"))
                ).orElseThrow(() -> new NotFoundException("User with email" + email + "not found"))
        ).orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));
    }

    @Transactional
    public Optional<List<Cart>> clearCart(String email) {
        return Optional.ofNullable(userService.get(email)
                .map(u -> {
                    cartRepo.deleteByCustomer(u);
                    return cartRepo.getByCustomer(u);
                }).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Transactional
    public void save(Cart c) { this.cartRepo.save(c); }
}
