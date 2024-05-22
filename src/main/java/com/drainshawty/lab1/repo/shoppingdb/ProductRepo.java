package com.drainshawty.lab1.repo.shoppingdb;

import com.drainshawty.lab1.model.shoppingdb.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product getById(long id);

    boolean existsByName(String name);

    List<Product> getByNameContainingIgnoreCase(String substring);

    Product getByName(String name);
}
