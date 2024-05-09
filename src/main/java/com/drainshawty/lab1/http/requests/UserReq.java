package com.drainshawty.lab1.http.requests;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserReq implements Serializable {
    @Email
    public String email;
    public String password;
    public String name;
}
