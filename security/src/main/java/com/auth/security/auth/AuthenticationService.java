package com.auth.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.auth.security.config.JwtService;
import com.auth.security.exception.EmailAlreadyExistsException;
import com.auth.security.exception.MissingFieldsException;
import com.auth.security.user.Role;
import com.auth.security.user.User;
import com.auth.security.user.UserRepository;
import com.auth.security.user.User.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    // Register user
    public AuthenticationResponse register(RegisterRequest request) {
        logger.info("Received registration request with isAdmin: {}", request.getIsAdmin());

        if (request.getFirstname() == null ||
                request.getLastname() == null ||
                request.getEmail() == null ||
                request.getPassword() == null) {
            throw new MissingFieldsException("All fields are required.");
        }

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        boolean isAdmin = Boolean.parseBoolean(request.getIsAdmin());
        logger.info("Inside isAdmin: {}", isAdmin);

        Role role = isAdmin ? Role.ADMIN : Role.USER;
        logger.info("Inside Role: {}", role);

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(Status.ACTIVE) // Set default status to ACTIVE
                .build();

        repository.save(user);

        logger.info("Inside Role: {}", user.getRole());
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .isAdmin(isAdmin) // Include isAdmin in the response
                .build();
    }

    // Authenticate user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check if the user is ACTIVE
        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Account is inactive. Please contact support.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);

        // Check if the user is an admin
        boolean isAdmin = user.getRole() == Role.ADMIN;

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .isAdmin(isAdmin) // Include isAdmin in the response
                .build();
    }
}
