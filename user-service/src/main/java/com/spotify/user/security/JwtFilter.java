package com.spotify.user.security;

import com.spotify.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    private final TokenBlacklist  tokenBlacklist;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService, TokenBlacklist tokenBlacklist) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (tokenBlacklist != null && tokenBlacklist.isBlacklisted (token)) {
               // log.warn ("Invalid token for username :{}",token);
                response.setStatus (HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter ().write ("{\"error\":\"Toekn is blacklisted\"}");
                return;
            }
            String username = jwtUtil.extractUsername(token,false);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtil.validateToken(token,false)) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }else {
                   //log.warn ("Invalid token for username :{}",username);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
