package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.entity.Role;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.repository.UserRepository;
import com.workintech.TwitterApi.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String firstName, String lastName, String userName, String email, String password) {
        // Kullanıcı adı veya email daha önce alınmış mı kontrolü
        if (userRepository.existsByUserName(userName)) {
            throw new RuntimeException("User with given username already exists: " + userName);
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with given email already exists: " + email);
        }

        String encodedPassword = passwordEncoder.encode(password);

        List<Role> roleList = new ArrayList<>();
        addRoleUser(roleList); // Varsayılan olarak USER rolü atıyoruz

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(roleList);

        return userRepository.save(user);
    }

    public void addRoleAdmin(List<Role> roleList) {
        Optional<Role> roleAdmin = roleRepository.findByAuthority(ROLE_ADMIN);

        if (roleAdmin.isEmpty()) {
            Role roleAdminEntity = new Role();
            roleAdminEntity.setAuthority(ROLE_ADMIN);
            roleList.add(roleRepository.save(roleAdminEntity));
        } else {
            roleList.add(roleAdmin.get());
        }
    }

    public void addRoleUser(List<Role> roleList) {
        Optional<Role> roleUser = roleRepository.findByAuthority(ROLE_USER);

        if (roleUser.isEmpty()) {
            Role roleUserEntity = new Role();
            roleUserEntity.setAuthority(ROLE_USER);
            roleList.add(roleRepository.save(roleUserEntity));
        } else {
            roleList.add(roleUser.get());
        }
    }
}