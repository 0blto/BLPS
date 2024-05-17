package com.drainshawty.lab1.repo;

import com.drainshawty.lab1.model.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepo extends CrudRepository<Privilege, Long> {
    Privilege getByName(String name);
}
