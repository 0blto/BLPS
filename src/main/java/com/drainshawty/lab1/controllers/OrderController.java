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

import java.util.Collections;

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

    @PostMapping(path = "/work/relocate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResp> changeStatus(@Valid @RequestBody OrderReq req) {
        return service.changeStatus(req.getId())
                .map(os -> new ResponseEntity<>(OrderResp.builder().order(os).msg("Success!").build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(OrderResp.builder().msg("Something went wrong.").build(), HttpStatus.CONFLICT));
    }

    @PostMapping(path = "/work/receive", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResp> receiveOrder(@Valid @RequestBody OrderReq req) throws Exception {
        return service.orderReceive(req.getId(), req.getProductId())
                .map(o -> new ResponseEntity<>(OrderResp.builder().order(
                        Collections.singletonList(o)
                ).msg("Success!").build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(OrderResp.builder().msg("Order must have status arrived").build(), HttpStatus.CONFLICT));
    }

    @PostMapping(path = "/work/return", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResp> returnOrder(@Valid @RequestBody OrderReq req) throws Exception {
        return service.orderReturn(req.getId(), req.getProductId())
                .map(o -> new ResponseEntity<>(OrderResp.builder().order(
                        Collections.singletonList(o)
                ).msg("Success!").build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(OrderResp.builder().msg("Order must have status arrived or received").build(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
