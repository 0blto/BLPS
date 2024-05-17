package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Role getByName(String name);
}
