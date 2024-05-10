package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {
    Product getById(long id);

    boolean existsByName(String name);

    List<Product> getByNameContaining(String substring);

    Product getByName(String name);
}
