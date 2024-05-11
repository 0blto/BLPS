package com.drainshawty.lab1.controllers;

import com.drainshawty.lab1.http.requests.OrderReq;
import com.drainshawty.lab1.http.responces.OrderResp;
import com.drainshawty.lab1.security.JWTUtil;
import com.drainshawty.lab1.services.CartService;
import com.drainshawty.lab1.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService service;
    JWTUtil jwtUtil;

    @Autowired
    public OrderController(OrderService service, JWTUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping(produces = "application/json")
    public ResponseEntity<OrderResp> createOrder(HttpServletRequest rawReq) {
        return service.createOrder(jwtUtil.decode(rawReq))
                .map(os -> new ResponseEntity<>(OrderResp.builder().order(os).msg("Success!").build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(OrderResp.builder().msg("Payment denied").build(), HttpStatus.CONFLICT));
    }

    @PostMapping(path = "/work", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResp> changeStatus(@Valid @RequestBody OrderReq req) {
        return service.changeStatus(req.getId(), req.getStatus())
                .map(os -> new ResponseEntity<>(OrderResp.builder().order(os).msg("Success!").build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(OrderResp.builder().msg("Something went wrong.").build(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
