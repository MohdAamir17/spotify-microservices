package com.spotify.user.service;


import com.spotify.user.security.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TokenBlacklist tokenBlacklist;

    public AuthService(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
            return "success";
        }
        return "no_token";
    }
}
