package com.drainshawty.lab1.services;

import com.drainshawty.lab1.exceptions.NotFoundException;
import com.drainshawty.lab1.model.shoppingdb.Cart;
import com.drainshawty.lab1.model.shoppingdb.CartPK;
import com.drainshawty.lab1.repo.shoppingdb.CartRepo;
import com.drainshawty.lab1.repo.shoppingdb.ProductRepo;
import com.drainshawty.lab1.repo.userdb.UserRepo;
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

    ProductRepo productRepo;
    UserRepo userRepo;
    CartRepo cartRepo;
    @Autowired
    public CartService(ProductRepo productRepo, UserRepo userRepo, CartRepo cartRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cartRepo = cartRepo;
    }

    @Transactional
    public Optional<List<Cart>> getUserCart(String email) {
        return Optional.ofNullable(userRepo.findByEmail(email)
                .map(u -> cartRepo.getByCartPK_CustomerId(u.getUserId()))
                .orElseThrow(() -> new NotFoundException("User not found!"))
        );
    }

    @Transactional
    public Optional<List<Cart>> addProduct(String email, Long productId) {
        return productRepo.findById(productId).map(p ->
                userRepo.findByEmail(email).map(u -> {
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
        return productRepo.findById(productId).map(p ->
                userRepo.findByEmail(email).map(u ->
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
        return Optional.ofNullable(userRepo.findByEmail(email)
                .map(u -> {
                    cartRepo.deleteByCartPK_CustomerId(u.getUserId());
                    return cartRepo.getByCartPK_CustomerId(u.getUserId());
                }).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Transactional
    public void save(Cart c) { this.cartRepo.save(c); }
}
