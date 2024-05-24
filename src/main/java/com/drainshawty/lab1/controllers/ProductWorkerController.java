package com.drainshawty.lab1.controllers;

import com.drainshawty.lab1.http.requests.ProductReq;
import com.drainshawty.lab1.http.responces.ProductResp;
import com.drainshawty.lab1.services.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(path = "/secured/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductWorkerController {

    ProductService service;

    @Autowired
    public ProductWorkerController(ProductService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('PRODUCTS_MANAGEMENT_PRIVILEGE')")
    @PutMapping(path = "add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProductResp> addProduct(@Valid @RequestBody ProductReq req) {
        return service.get(req.getName())
                .map(u -> new ResponseEntity<>(
                        ProductResp.builder().msg("Product already exist").build(),
                        HttpStatus.CONFLICT)
                ).orElseGet(() -> {
                    val p = service.add(req.getName(), req.getDescription(), req.getNumber(), req.getPrice()).get();
                    return new ResponseEntity<>(
                            ProductResp.builder()
                                    .products(Collections.singletonList(p)).msg(String.format("Product %s added", p.getName()))
                                    .build(),
                            HttpStatus.CREATED);
                });
    }
}
