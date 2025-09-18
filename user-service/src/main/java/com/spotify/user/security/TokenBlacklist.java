package com.spotify.user.security;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<> ());

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void remove(String token) {
        blacklist.remove(token);
    }
}
