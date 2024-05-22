package com.drainshawty.lab1.repo.userdb;

import com.drainshawty.lab1.model.userdb.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User getByEmail(String email);

    User getByUserId(long id);

    boolean existsByEmail(String email);
}
