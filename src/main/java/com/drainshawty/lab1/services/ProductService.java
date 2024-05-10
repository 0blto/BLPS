package com.drainshawty.lab1.services;


import com.drainshawty.lab1.model.Product;
import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.repo.ProductRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepo repo;
    @Autowired
    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public Optional<Product> add(String name, String description, Long number, Long price) {
        val u = Product.builder().name(name).description(description)
                .number(number).price(price).build();
        this.save(u);
        return this.get(name);
    }

    public Optional<Product> get(long id) { return Optional.ofNullable(repo.getById(id)); }

    public Optional<Product> get(String name) { return Optional.ofNullable(repo.getByName(name)); }

    public Optional<List<Product>> like(String name) { return Optional.ofNullable(repo.getByNameContaining(name)); }

    public List<Product> getAll() { return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList()); }

    public boolean exist(String name) { return repo.existsByName(name); }

    @Transactional
    public void save(Product p) { this.repo.save(p); }
}
