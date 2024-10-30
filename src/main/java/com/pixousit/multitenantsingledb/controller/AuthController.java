package com.pixousit.multitenantsingledb.controller;

import com.pixousit.multitenantsingledb.config.TenantContext;
import com.pixousit.multitenantsingledb.dto.LoginRequest;
import com.pixousit.multitenantsingledb.dto.LoginResponse;
import com.pixousit.multitenantsingledb.dto.RegisterRequest;
import com.pixousit.multitenantsingledb.entity.User;
import com.pixousit.multitenantsingledb.repository.UserRepository;
import com.pixousit.multitenantsingledb.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            throw new RuntimeException("UserDetails not found in authentication object");
        }
        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            TenantContext.setTenantId(user.getTenantId());
        });
        String tenantId = TenantContext.getTenantId();
        String jwt = jwtUtil.generateToken(userDetails, tenantId);
        return new LoginResponse(jwt);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return "Username already exists!";
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return "User registered successfully!";
    }
}
