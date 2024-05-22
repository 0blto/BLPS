package com.drainshawty.lab1.config;

import com.drainshawty.lab1.model.userdb.Privilege;
import com.drainshawty.lab1.model.userdb.Role;
import com.drainshawty.lab1.model.userdb.User;
import com.drainshawty.lab1.repo.userdb.PrivilegeRepo;
import com.drainshawty.lab1.repo.userdb.RoleRepo;
import com.drainshawty.lab1.repo.userdb.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    @NonFinal
    @Value("${admin.name}")
    String ADMIN_NAME;

    @NonFinal
    @Value("${admin.password}")
    String ADMIN_PASSWORD;

    @NonFinal
    @Value("${spring.mail.username}")
    String ADMIN_EMAIL;

    @NonFinal
    boolean alreadySetup = false;

    UserRepo userRepository;

    RoleRepo roleRepository;

    PrivilegeRepo privilegeRepository;

    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserRepo userRepository, RoleRepo roleRepository, PrivilegeRepo privilegeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;
        Privilege viewPrivilege = createPrivilegeIfNotFound("VIEW_PRIVILEGE");
        Privilege cartPrivilege = createPrivilegeIfNotFound("CART_PRIVILEGE");
        Privilege orderPrivilege = createPrivilegeIfNotFound("ORDER_PRIVILEGE");

        Privilege orderManagementPrivilege = createPrivilegeIfNotFound("ORDER_MANAGEMENT_PRIVILEGE");
        Privilege productsManagementPrivilege = createPrivilegeIfNotFound("PRODUCTS_MANAGEMENT_PRIVILEGE");


        Privilege hirePrivilege = createPrivilegeIfNotFound("HIRE_PRIVILEGE");

        createRoleIfNotFound("ROLE_ADMIN", Collections.singletonList(hirePrivilege));
        createRoleIfNotFound("ROLE_STAFF", Arrays.asList(orderManagementPrivilege, productsManagementPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(viewPrivilege, orderPrivilege, cartPrivilege));
        Role adminRole = roleRepository.getByName("ROLE_ADMIN");
        User user = User.builder()
                .email(ADMIN_EMAIL)
                .name(ADMIN_NAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .roles(Collections.singletonList(adminRole))
                .build();
        userRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.getByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public void createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.getByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }
}
