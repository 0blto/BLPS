package com.drainshawty.lab1.model.userdb;

import com.drainshawty.lab1.model.userdb.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @ManyToMany(mappedBy = "privileges")
    Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}
