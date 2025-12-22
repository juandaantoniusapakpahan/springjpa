package com.backend.springjpa.service;

import com.backend.springjpa.dto.LoginUserDto;
import com.backend.springjpa.dto.RegisterUserDto;
import com.backend.springjpa.dto.UserRegisteredEvent;
import com.backend.springjpa.entity.User;
import com.backend.springjpa.entity.VerificationToken;
import com.backend.springjpa.repository.UserRepository;
import com.backend.springjpa.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final VerificationTokenRepository tokenRepository;

    public AuthenticationService(
            ApplicationEventPublisher eventPublisher, UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            VerificationTokenRepository verificationTokenRepository) {
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = verificationTokenRepository;
    }

    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        user = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        VerificationToken verificationToken = VerificationToken.builder()
                        .token(jwtToken)
                        .expiredAt(LocalDateTime.now().plusMinutes(jwtExpiration))
                                .user(user).build();
        tokenRepository.save(verificationToken);
        eventPublisher.publishEvent(
                new UserRegisteredEvent(
                        user.getEmail(),
                        user.getFullName(),
                        jwtToken
                )
        );
        return user;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}