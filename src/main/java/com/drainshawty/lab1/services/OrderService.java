package com.drainshawty.lab1.services;


import com.drainshawty.lab1.exceptions.NotFoundException;
import com.drainshawty.lab1.model.shoppingdb.Order;
import com.drainshawty.lab1.model.shoppingdb.OrderPK;
import com.drainshawty.lab1.model.shoppingdb.Product;
import com.drainshawty.lab1.repo.shoppingdb.OrderRepo;
import com.drainshawty.lab1.util.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    private Order.Status getNextStatus(Order.Status status) {
        switch (status) {
            case CREATED -> {
                return Order.Status.PAID;
            }
            case PAID -> {
                return Order.Status.SHIPPING;
            }
            case SHIPPING -> {
                return Order.Status.ARRIVED;
            }
            default -> {
                return null;
            }
        }
    }

    ProductService productService;
    UserService userService;
    CartService cartService;
    OrderRepo orderRepo;
    RestTemplate restTemplate;

    @Autowired
    public OrderService(ProductService productService, UserService userService, CartService cartService, OrderRepo orderRepo, RestTemplate restTemplate) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderRepo = orderRepo;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Optional<List<Order>> changeStatus(Long id) {
        List<Order> orders = orderRepo.getByOrderPK_OrderId(id);
        if (orders.isEmpty()) throw new NotFoundException("Order not found");
        return Optional.of(orders.stream().peek(o -> {
            if (getNextStatus(o.getStatus()) == null) return;
            o.setStatus(getNextStatus(o.getStatus()));
            save(o);
        }).toList());
    }

    @Transactional
    public Optional<Order> orderReceive(Long orderId, Long productId) {
        Optional<Order> o = Optional.ofNullable(orderRepo.getByOrderPK_OrderIdAndOrderPK_ProductId(orderId, productId));
        if (o.isPresent()) {
            Order order = o.get();
            if (order.getStatus().equals(Order.Status.ARRIVED)) {
                order.setStatus(Order.Status.RECEIVED);
                save(order);
                return Optional.of(order);
            }
            return Optional.empty();
        }
        throw new NotFoundException("Order not found");

    }

    @Transactional
    public Optional<Order> orderReturn(Long orderId, Long productId) {
        Optional<Order> o = Optional.ofNullable(orderRepo.getByOrderPK_OrderIdAndOrderPK_ProductId(orderId, productId));
        if (o.isPresent()) {
            Order order = o.get();
            if (List.of(Order.Status.RECEIVED, Order.Status.ARRIVED).contains(order.getStatus())) {
                order.setStatus(Order.Status.RETURNED);
                Product product = order.getProduct();
                product.setNumber(product.getNumber() + order.getQuantity());
                productService.save(product);
                save(order);
                return Optional.of(order);
            }
            return Optional.empty();
        }
        throw new NotFoundException("Order not found");
    }

    @Transactional
    public Optional<List<Order>> createOrder(String email) {
        Long currentId = SequenceGenerator.getNext();
        return Optional.ofNullable(userService.get(email)
                .map(u -> cartService.getUserCart(email)
                        .map(cs -> {
                            if (cs.isEmpty()) throw new NotFoundException("User cart is empty");
                            cs.stream()
                                    .map(c -> Order.builder()
                                            .orderPK(new OrderPK(
                                                    currentId,
                                                    u.getUserId(),
                                                    c.getProduct().getId()
                                            ))
                                            .product(c.getProduct())
                                            .quantity(c.getQuantity())
                                            .status(Order.Status.CREATED).build()
                                    ).forEach(this::save);
                            cartService.clearCart(email);
                            orderRepo.getByOrderPK_OrderId(currentId).forEach(o ->
                                    productService.get(o.getProduct().getId())
                                            .ifPresent(p -> {
                                                p.setNumber(p.getNumber() - o.getQuantity());
                                                productService.save(p);
                                            })
                            );
                            return orderRepo.getByOrderPK_OrderId(currentId);
                        }).orElseThrow(() -> new NotFoundException("Something went wrong!"))
                ).orElseThrow(() -> new NotFoundException("Unable to create order")));
    }

    @Transactional
    public void save(Order order) {orderRepo.save(order);}
}
