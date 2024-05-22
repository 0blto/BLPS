package com.drainshawty.lab1.repo.userdb;

import com.drainshawty.lab1.model.userdb.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    Privilege getByName(String name);
}
