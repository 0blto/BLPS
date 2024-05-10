package com.drainshawty.lab1.controllers;

import com.drainshawty.lab1.http.requests.UserReq;
import com.drainshawty.lab1.http.responces.UserResp;
import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.security.JWTUtil;
import com.drainshawty.lab1.services.SuperuserService;
import com.drainshawty.lab1.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping(path = "/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SuperuserController {
    JWTUtil jwtUtil;
    SuperuserService service;
    UserService userService;
    AuthenticationManager auth;

    @Autowired
    public SuperuserController(JWTUtil jwtUtil, SuperuserService service, UserService userService, AuthenticationManager auth) {
        this.auth = auth;
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PatchMapping(path = "hire", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResp> hire(@Valid @RequestBody UserReq req) {

        try {
            return service.hire(req.email)
                    .map(u -> new ResponseEntity<>(
                                UserResp.builder().users(Collections.singletonList(u)).msg("Success!").build(),
                                HttpStatus.OK
                        )
                    )
                    .orElse(new ResponseEntity<>(
                            UserResp.builder().msg("User not found.").build(),
                            HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return new ResponseEntity<>(
                    UserResp.builder().msg(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
