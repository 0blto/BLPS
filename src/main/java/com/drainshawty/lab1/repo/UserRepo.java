package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    User getByEmail(String email);

    User getByUserId(long id);

    boolean existsByEmail(String email);

}
