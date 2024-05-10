package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.Cart;
import com.drainshawty.lab1.model.CartPK;
import com.drainshawty.lab1.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface CartRepo extends CrudRepository<Cart, CartPK> {
    List<Cart> getByCustomer(User customer);

    void deleteByCartPK(CartPK pk);

    void deleteByCustomer(User user);
}
