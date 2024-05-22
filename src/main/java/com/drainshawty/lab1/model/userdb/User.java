package com.drainshawty.lab1.model.userdb;

import com.drainshawty.lab1.model.shoppingdb.Cart;
import com.drainshawty.lab1.model.shoppingdb.Order;
import com.drainshawty.lab1.serializers.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name="usr",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")}
)
@JsonSerialize(using = UserSerializer.class)
public class User implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long userId;

    @Email
    @NotNull
    String email;

    @NotNull
    String password;

    @NotNull
    String name;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    Collection<Role> roles;
}


