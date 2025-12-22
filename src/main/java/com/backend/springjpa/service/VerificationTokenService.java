package com.backend.springjpa.service;

import com.backend.springjpa.entity.User;
import com.backend.springjpa.entity.VerificationToken;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.repository.UserRepository;
import com.backend.springjpa.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public VerificationTokenService(VerificationTokenRepository repository,
                                    JwtService jwtService,
                                    UserDetailsService userDetailsService,
                                    UserRepository userRepository) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }
    @Transactional
    public void findToken(String token) {
        VerificationToken verificationToken = repository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Token invalid"));

        final String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            throw new BadRequestException("Token invalid");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("Token invalid"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new BadRequestException("Token expired");
        }

        user.setVerificationToken(null);
        verificationToken.setUser(null);

        user.setEnabled(true);
        userRepository.save(user);

        repository.delete(verificationToken);
    }
}
