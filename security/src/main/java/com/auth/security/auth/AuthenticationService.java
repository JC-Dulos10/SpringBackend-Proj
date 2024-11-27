package com.auth.security.auth;

import com.auth.security.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.auth.security.config.JwtService;
import com.auth.security.exception.EmailAlreadyExistsException;
import com.auth.security.exception.MissingFieldsException;
import com.auth.security.user.Role;
import com.auth.security.user.User;
import com.auth.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    //register user
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
                .build();
        repository.save(user);

        logger.info("Inside Role: {}", user.getRole());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .isAdmin(isAdmin)
                .build();


    }

    //Authenticate user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();

            var jwtToken = jwtService.generateToken(user);

            // Check if the user is an admin
            boolean isAdmin = user.getRole() == Role.ADMIN;

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .isAdmin(isAdmin)
                    .build();
        }catch (BadCredentialsException ex) {
            throw new AuthenticationException("Incorrect email or password");
        }
    }
}
