package com.spotify.user.controller;

import com.spotify.user.Dto.RegisterRequestDto;
import com.spotify.user.security.JwtUtil;
import com.spotify.user.service.AuthService;
import com.spotify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping ("/users")
public class UserController {

    private final UserService userService;

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, AuthService authService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping ("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        return userService.register (request);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        return userService.findByUsername(username)
                .filter(u -> new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                        .matches(password, u.getPasswordHash()))
                .map(u -> ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(username)
                        ,"refreshToken",jwtUtil.generateRefreshToken (username))))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @GetMapping ("/profile")
    public ResponseEntity<?> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        Map<String, Object> userProfile = userService.getProfile (auth);
        return ResponseEntity.ok (userProfile);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String result = authService.logout(request);
        if ("success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No token found"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return userService.refreshAccessToken(refreshToken);
    }
}
