package com.drainshawty.lab1.model;

import com.drainshawty.lab1.serializers.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name="usr",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")}
)
@JsonSerialize(using = UserSerializer.class)
public class User implements Serializable, UserDetails {
    public enum Role {
        USER,
        WORKER,
        ADMIN
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    long userId;

    @Email
    @NotNull
    String email;

    @NotNull
    String password;

    @NotNull
    String name;

    @OneToMany(mappedBy = "customer")
    Set<Cart> cart;

    @OneToMany(mappedBy = "customer")
    Set<Order> order;



    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    Set<Role> roles;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

    @Override public String getPassword() { return password; }

    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }
}


