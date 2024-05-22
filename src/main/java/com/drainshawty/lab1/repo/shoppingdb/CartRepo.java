package com.drainshawty.lab1.repo.shoppingdb;

import com.drainshawty.lab1.model.shoppingdb.Cart;
import com.drainshawty.lab1.model.shoppingdb.CartPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, CartPK> {
    List<Cart> getByCartPK_CustomerId(Long id);

    void deleteByCartPK(CartPK pk);

    void deleteByCartPK_CustomerId(Long id);
}
