package com.teerasak.crudapp.controller;

import com.teerasak.crudapp.entitity.*;
import com.teerasak.crudapp.service.UserService;
import com.teerasak.crudapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register-user")
    public ResponseEntity<ResponseModel> registerUser(@RequestBody RegisterModel model) {
        try {
            User user = userService.registerUser(model.username, model.email, model.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel("Success", "User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel("Error", e.getMessage(), null));
        }
    }

    @PostMapping("/register-manager")
    public ResponseEntity<ResponseModel> registerManager(@RequestBody RegisterModel model) {
        try {
            User user = userService.registerManager(model.username, model.email, model.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel("Success", "User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel("Error", e.getMessage(), null));
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ResponseModel> registerAdmin(@RequestBody RegisterModel model) {
        try {
            User user = userService.registerAdmin(model.username, model.email, model.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel("Success", "User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel("Error", e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(@RequestBody LoginModel model) {
        Optional<User> user = userService.findByUserName(model.getUsername());

        if (user.isPresent() && passwordEncoder.matches(model.getPassword(), user.get().getPassword())) {
            List<String> roles = user.get().getRoles().stream().map(role -> role.getRoleName().name()).toList();
            String token = jwtUtil.generateToken(user.get().getUsername(), roles);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    "Success",
                    "Login successful",
                    Map.of(
                            "token", token,
                            "userName", user.get().getUsername(),
                            "email", user.get().getEmail(),
                            "roles", roles
                    ).toString())
            );
        } else {
            return ResponseEntity.status(401).body(new ResponseModel("Error", "Invalid username or password", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseModel> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel("Success", "Logged out successfully", null));
    }
}
