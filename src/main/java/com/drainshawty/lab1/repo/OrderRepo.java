package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, OrderPK> {
    List<Order> getByCustomer(User customer);

    List<Order> getByOrderPK_OrderId(Long id);
}
