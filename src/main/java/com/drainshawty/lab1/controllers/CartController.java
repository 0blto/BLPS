package com.drainshawty.lab1.controllers;

import com.drainshawty.lab1.exceptions.NotFoundException;
import com.drainshawty.lab1.http.requests.CartReq;
import com.drainshawty.lab1.http.responces.CartResp;
import com.drainshawty.lab1.security.JWTUtil;
import com.drainshawty.lab1.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService service;
    JWTUtil jwtUtil;

    @Autowired
    public CartController(CartService service, JWTUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasAuthority('CART_PRIVILEGE')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<CartResp> getCart(HttpServletRequest rawReq) {
        return service.getUserCart(jwtUtil.decode(rawReq))
                .map(carts -> new ResponseEntity<>(
                        CartResp.builder().cart(carts).build(),
                        HttpStatus.OK
                )).orElseGet(() -> new ResponseEntity<>(
                        CartResp.builder().msg("Product or User not found").build(),
                        HttpStatus.CONFLICT
                ));
    }

    @PreAuthorize("hasAuthority('CART_PRIVILEGE')")
    @PutMapping(path = "add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartResp> add(@Valid @RequestBody CartReq req, HttpServletRequest rawReq) {
        return service.addProduct(jwtUtil.decode(rawReq), req.getIdentifier())
                .map(carts -> new ResponseEntity<>(
                CartResp.builder().cart(carts).build(),
                HttpStatus.OK
        )).orElseGet(() -> new ResponseEntity<>(
                CartResp.builder().msg("Product or User not found").build(),
                HttpStatus.CONFLICT
        ));
    }

    @PreAuthorize("hasAuthority('CART_PRIVILEGE')")
    @DeleteMapping(path = "remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartResp> remove(@Valid @RequestBody CartReq req, HttpServletRequest rawReq) {
        return service.removeOne(jwtUtil.decode(rawReq), req.getIdentifier())
                .map(carts -> new ResponseEntity<>(
                        CartResp.builder().cart(carts).build(),
                        HttpStatus.OK
                )).orElseGet(() -> new ResponseEntity<>(
                        CartResp.builder().msg("Product or User not found").build(),
                        HttpStatus.CONFLICT
                ));
    }

    @PreAuthorize("hasAuthority('CART_PRIVILEGE')")
    @DeleteMapping(path = "clear", produces = "application/json")
    public ResponseEntity<CartResp> clearCart(HttpServletRequest rawReq) {
        return service.clearCart(jwtUtil.decode(rawReq))
                .map(carts -> new ResponseEntity<>(
                        CartResp.builder().cart(carts).build(),
                        HttpStatus.OK
                )).orElseGet(() -> new ResponseEntity<>(
                        CartResp.builder().msg("Product or User not found").build(),
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
