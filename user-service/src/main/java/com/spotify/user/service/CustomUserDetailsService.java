package com.spotify.user.service;

import com.spotify.user.entity.CustomUserDetails;
import com.spotify.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    private final UserRepository userRepository; // Your JPA repo

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.spotify.user.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

       /* return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())   // must be encoded with BCrypt
                .authorities(user.getRole())        // e.g., "ROLE_USER" or "ROLE_ADMIN"
                .build();*/

        return new CustomUserDetails (user);
    }

}
