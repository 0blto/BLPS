package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.userdb.User;
import com.drainshawty.lab1.repo.userdb.RoleRepo;
import com.drainshawty.lab1.repo.userdb.UserRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SuperuserService {
    UserRepo repo;
    BCryptPasswordEncoder encoder;
    RoleRepo roleRepo;


    @Transactional
    public Optional<User> hire(String email) throws Exception {
        User user = repo.getByEmail(email);
        if (user.getRoles().contains(roleRepo.getByName("ROLE_STAFF"))
                || user.getRoles().contains(roleRepo.getByName("ROLE_ADMIN")))
            throw new Exception("User already hired!");
        user.getRoles().add(roleRepo.getByName("ROLE_STAFF"));
        repo.save(user);
        return Optional.of(user);
    }
}
