package com.drainshawty.lab1.controllers;

import com.drainshawty.lab1.http.requests.ProductReq;
import com.drainshawty.lab1.http.requests.UserReq;
import com.drainshawty.lab1.http.responces.ProductResp;
import com.drainshawty.lab1.http.responces.UserResp;
import com.drainshawty.lab1.services.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(path = "/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping(path = "all", produces = "application/json")
    public ResponseEntity<ProductResp> getAll() {
        return new ResponseEntity<>(
                ProductResp.builder().products(service.getAll()).build(),
                HttpStatus.OK);
    }

    @GetMapping(path = "{productId}", produces = "application/json")
    public ResponseEntity<ProductResp> getProduct(@PathVariable long productId) {
        return service.get(productId)
                .map(p -> new ResponseEntity<>(
                        ProductResp.builder().products(Collections.singletonList(p)).build(),
                        HttpStatus.OK)
                ).orElse(new ResponseEntity<>(
                        ProductResp.builder().msg("Product with this id didn't exist").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @GetMapping(path = "/search/{productName}", produces = "application/json")
    public ResponseEntity<ProductResp> searchProduct(@PathVariable String productName) {
        return service.get(productName)
                .map(p -> new ResponseEntity<>(
                        ProductResp.builder().msg("Exactly").products(Collections.singletonList(p)).build(),
                        HttpStatus.OK)
                ).orElse(service.like(productName).map(ps -> new ResponseEntity<>(
                                ProductResp.builder().msg("Sounds like you are looking for").products(ps).build(),
                                HttpStatus.OK)
                        ).orElse(new ResponseEntity<>(
                        ProductResp.builder().msg("Product with this name didn't exist").build(),
                        HttpStatus.BAD_REQUEST))
                );
    }
}
