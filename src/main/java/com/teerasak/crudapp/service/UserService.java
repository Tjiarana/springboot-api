package com.teerasak.crudapp.service;

import com.teerasak.crudapp.entitity.Role;
import com.teerasak.crudapp.entitity.RoleName;
import com.teerasak.crudapp.entitity.User;
import com.teerasak.crudapp.repository.RoleRepository;
import com.teerasak.crudapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Lazy private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                        .collect(Collectors.toList())
        );
    }

    public User registerUser(String username, String email, String password) {
        return registerUserWithRole(username, email, password, RoleName.USER);
    }

    public User registerManager(String username, String email, String password) {
        return registerUserWithRole(username, email, password, RoleName.MANAGER);
    }

    public User registerAdmin(String username, String email, String password) {
        return registerUserWithRole(username, email, password, RoleName.ADMIN);
    }

    private User registerUserWithRole(String username, String email, String password, RoleName roleName) {
        try {
            if(userRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            if(userRepository.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));

            user.setRoles(new ArrayList<>(List.of(role)));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register user : " + e.getMessage());
        }
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
