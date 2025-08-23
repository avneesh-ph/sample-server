package com.pharynxai.julius.server.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            .signWith(jwtSigningKey())
            .compact();
    }

    public void addJwtToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("access-token", token);
        cookie.setHttpOnly(true);   // prevent JS access
        cookie.setSecure(false);     // send only over HTTPS (set false for local dev)
        cookie.setPath("/");        // valid for all endpoints
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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
