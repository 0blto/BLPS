package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.repo.UserRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserService {
    UserRepo repo;
    BCryptPasswordEncoder encoder;
    EmailService mailer;

    @Autowired
    public UserService(UserRepo repo, BCryptPasswordEncoder encoder, EmailService mailer) {
        this.repo = repo;
        this.encoder = encoder;
        this.mailer = mailer;
    }

    @Transactional
    public Optional<User> add(String email, String password, String name) {
        val u = User.builder().email(email).name(name).roles(Set.of(User.Role.USER)).password(encoder.encode(password)).build();
        this.save(u);
        try {
            mailer.send(email, "Register", "yeah!");
        } catch (MailSendException e) { System.out.println("[ERROR] " + e.getLocalizedMessage()); }
        return this.get(email);
    }

    @Transactional
    public void delete(String email) {
        repo.delete(this.repo.getByEmail(email));
        try {
            mailer.send(email, "Goodbye", ":(");
        } catch (MailSendException e) { System.out.println("[ERROR] " + e.getLocalizedMessage()); }
    }

    @Transactional
    public boolean restorePassword(String email) {
        try {
            mailer.send(email, "Your password", "There could be request to restore password");
            return true;
        } catch (MailSendException e) {
            System.out.println("[ERROR] " + e.getLocalizedMessage());
            return false;
        }
    }

    public List<User> getAll() { return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList()); }

    public Optional<User> get(String email) { return Optional.ofNullable(repo.getByEmail(email)); }

    public Optional<User> get(long id) { return Optional.ofNullable(repo.getByUserId(id)); }

    public boolean exist(String email) { return repo.existsByEmail(email); }

    @Transactional
    public void save(User u) { this.repo.save(u); }
}
