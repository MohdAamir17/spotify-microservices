package com.spotify.user.service;


import com.spotify.user.Dto.RegisterRequestDto;
import com.spotify.user.entity.User;
import com.spotify.user.repository.UserRepository;
import com.spotify.user.security.JwtUtil;
import com.spotify.user.security.TokenBlacklist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    private final TokenBlacklist tokenBlacklist;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo, TokenBlacklist tokenBlacklist, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.repo = repo;
        this.tokenBlacklist = tokenBlacklist;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> register(RegisterRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder ().encode(request.getPassword()));

        repo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Map<String, Object> getProfile(Authentication auth ) {

        String username = auth.getName();
        Optional<User> userOpt = repo.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Map.of("error", "User not found");
        }

        User user = userOpt.get();
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "authorities", auth.getAuthorities().toString()
        );
    }

    public ResponseEntity<?> refreshAccessToken(String refreshToken) {

        if (refreshToken == null || tokenBlacklist.isBlacklisted(refreshToken) || !jwtUtil.validateToken (refreshToken,true)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken,true);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }

        // Optional: rotate refresh token for extra security
        String newAccessToken = jwtUtil.generateToken (username);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", refreshToken // or generate new refresh token if rotating
        ));
    }
}
