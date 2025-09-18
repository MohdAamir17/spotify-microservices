package com.spotify.user.security;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

  //  private static final long ACCESS_TOKEN_EXPIRATION = 10 * 60 * 1000; //10 MINS

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Key getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt (new Date ())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //=========Refresh token===========
    public String generateRefreshToken(String username) {
        return Jwts.builder ()
                .setSubject (username)
                .setIssuedAt (new Date ())
                .setExpiration (new Date (System.currentTimeMillis () + refreshExpiration))
                .signWith (getRefreshSigningKey (),SignatureAlgorithm.HS256)
                .compact ();
    }

    public String extractUsername(String token, boolean isRefresh) {
        try{
            Key key = isRefresh ? getRefreshSigningKey () : getSigningKey ();
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }

    }

    public boolean validateToken(String token, boolean isRefresh) {
        try {
            Key key = isRefresh ? getRefreshSigningKey() : getSigningKey();
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(60)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            e.printStackTrace ();
            //log.warn("Token expired: {}", e.getMessage());
        } catch (JwtException e) {
            e.printStackTrace ();
            //log.error("Token validation failed: {}", e.getMessage());
        }
        return false;
    }


}

