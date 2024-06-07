package com.drainshawty.lab1.repo.shoppingdb;

import com.drainshawty.lab1.model.shoppingdb.Order;
import com.drainshawty.lab1.model.shoppingdb.OrderPK;
import com.drainshawty.lab1.model.userdb.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, OrderPK> {
    List<Order> getByOrderPK_OrderId(Long id);

    Order getByOrderPK_OrderIdAndOrderPK_ProductId(Long orderId, Long productId);

    List<Order> getAllByStatus(Order.Status status);
}
