package com.drainshawty.lab1.services;

import com.drainshawty.lab1.model.EmailData;
import com.drainshawty.lab1.model.userdb.User;
import com.drainshawty.lab1.repo.userdb.RoleRepo;
import com.drainshawty.lab1.repo.userdb.UserRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService {
    UserRepo repo;
    RoleRepo roleRepo;
    BCryptPasswordEncoder encoder;
    KafkaProducer kafkaProducer;

    @Transactional
    public Optional<User> add(String email, String password, String name) {
        val u = User.builder().email(email).name(name).roles(Set.of(roleRepo.getByName("ROLE_USER"))).password(encoder.encode(password)).build();
        this.save(u);
        kafkaProducer.sendMessage(
                EmailData.builder()
                        .receiver(email)
                        .topic("Register")
                        .message("Success!")
                        .build()
        );
        return this.get(email);
    }

    @Transactional
    public void delete(String email) {
        repo.delete(this.repo.getByEmail(email));
        kafkaProducer.sendMessage(
                EmailData.builder()
                        .receiver(email)
                        .topic("Bye bye")
                        .message("(")
                        .build()
        );
    }

    @Transactional
    public void restorePassword(String email) {
        kafkaProducer.sendMessage(
                EmailData.builder()
                        .receiver(email)
                        .topic("Restore password")
                        .message("JOKE")
                        .build()
        );
    }

    public List<User> getAll() { return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList()); }

    public Optional<User> get(String email) { return Optional.ofNullable(repo.getByEmail(email)); }

    public Optional<User> get(long id) { return Optional.ofNullable(repo.getByUserId(id)); }

    public boolean exist(String email) { return repo.existsByEmail(email); }

    @Transactional
    public void save(User u) { this.repo.save(u); }
}
