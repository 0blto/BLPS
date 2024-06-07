package com.drainshawty.lab1.controllers;


import com.drainshawty.lab1.http.requests.UserReq;
import com.drainshawty.lab1.http.responces.UserResp;
import com.drainshawty.lab1.security.JWTUtil;
import com.drainshawty.lab1.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    JWTUtil jwtUtil;
    UserService service;
    AuthenticationManager auth;

    @Autowired
    public UserController(JWTUtil jwtUtil, UserService service, AuthenticationManager auth) {
        this.auth = auth;
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(path = "all", produces = "application/json")
    public ResponseEntity<UserResp> getAll() {
        return new ResponseEntity<>(
                UserResp.builder().users(service.getAll()).build(),
                HttpStatus.OK);
    }

    @GetMapping(path = "{userId}", produces = "application/json")
    public ResponseEntity<UserResp> getUser(@PathVariable long userId) {
        return service.get(userId)
                .map(u -> new ResponseEntity<>(
                        UserResp.builder().users(Collections.singletonList(u)).build(),
                        HttpStatus.OK)
                ).orElse(new ResponseEntity<>(
                        UserResp.builder().msg("User with this id didn't exist").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @PostMapping(path = "login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> login(@Valid @RequestBody UserReq req) {
        return service.get(req.email)
                .map(u -> {
                    auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
                    return new ResponseEntity<>(
                            UserResp.builder()
                                    .token(jwtUtil.generateToken(u.getEmail(), Collections.singletonList("USER")))
                                    .build(),
                            HttpStatus.ACCEPTED);
                }).orElse(new ResponseEntity<>(
                        UserResp.builder().msg("User didn't exist. Check email and password").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @PutMapping(path = "register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> register(@Valid @RequestBody UserReq req) {
        return service.get(req.email)
                .map(u -> new ResponseEntity<>(
                        UserResp.builder().msg("User already exist").build(),
                        HttpStatus.CONFLICT)
                ).orElseGet(() -> {
                    val u = service.add(req.email, req.password, req.name).get();
                    auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
                    return new ResponseEntity<>(
                            UserResp.builder()
                                    .token(jwtUtil.generateToken(u.getEmail(), Collections.singletonList("USER")))
                                    .build(),
                            HttpStatus.CREATED);
                });
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping(path = "modify", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> modify(@Valid @RequestBody UserReq req, HttpServletRequest rawReq) {
        return (service.get(jwtUtil.decode(rawReq)))
                .map(u -> {
                    if (req.email != null)
                        if (service.exist(req.email))
                            return new ResponseEntity<>(
                                    UserResp.builder().msg("Mail is busy").build(),
                                    HttpStatus.CONFLICT);
                        else u.setEmail(req.email);
                    Optional.ofNullable(req.name).ifPresent(u::setName);
                    Optional.ofNullable(req.password).ifPresent(u::setPassword);
                    service.save(u);
                    return new ResponseEntity<>(
                            UserResp.builder().token(jwtUtil.generateToken(req.email, Collections.singletonList("USER"))).build(),
                            HttpStatus.ACCEPTED);
                }).orElse(new ResponseEntity<>(
                        UserResp.builder().msg("User didn't exist").build(),
                        HttpStatus.UNAUTHORIZED));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> delete(@Valid @RequestBody UserReq req, HttpServletRequest rawReq) {
        return (service.get(jwtUtil.decode(rawReq)))
                .map(u -> {
                    System.out.println(u.getEmail());
                    auth.authenticate(new UsernamePasswordAuthenticationToken(u.getEmail(), req.password));
                    service.delete(u.getEmail());
                    return new ResponseEntity<>(
                            UserResp.builder().msg("Successful delete account").build(),
                            HttpStatus.OK);
                }).orElse(new ResponseEntity<>(
                        UserResp.builder().msg("Wrong session token").build(),
                        HttpStatus.UNAUTHORIZED));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "restore", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> restore(@Valid @RequestBody UserReq req) {
        if (service.exist(req.email)) {
            service.restorePassword(req.email);
            return new ResponseEntity<>(UserResp.builder().msg("Restore email was send").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(UserResp.builder().msg("You are not a user!").build(), HttpStatus.OK);
    }
}
