package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.repo.UserRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SuperuserService {
    UserRepo repo;
    BCryptPasswordEncoder encoder;
    EmailService mailer;

    @Autowired
    public SuperuserService(UserRepo repo, BCryptPasswordEncoder encoder, EmailService mailer) {
        this.repo = repo;
        this.encoder = encoder;
        this.mailer = mailer;
    }

    @Transactional
    public Optional<User> hire(String email) throws Exception {
        User user = repo.getByEmail(email);
        if (user.getRoles().contains(User.Role.EDITOR))
            throw new Exception("User already hired!");
        user.getRoles().add(User.Role.EDITOR);
        repo.save(user);
        return Optional.of(user);
    }
}
