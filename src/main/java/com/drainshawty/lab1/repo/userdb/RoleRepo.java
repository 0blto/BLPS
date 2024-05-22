package com.drainshawty.lab1.repo.userdb;

import com.drainshawty.lab1.model.userdb.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role getByName(String name);
}
