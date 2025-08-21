package com.pharynxai.julius.server.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
    
    @Value("${environment.secret}")
    private String SECRET;

    public SecretKey jwtSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        // Jwts.builder().signWith(key); //or signWith(Key, SignatureAlgorithm)
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hr
            .signWith(SignatureAlgorithm.HS256, jwtSigningKey())
            .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(jwtSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractEmail(token).equals(userDetails.getUsername());
    }
}
