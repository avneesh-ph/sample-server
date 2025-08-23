package com.pharynxai.julius.server.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private JwtTokenUtil jwtTokenUtil;
    private UsersDetailsService usersDetailsService;

    public JwtAuthFilter(JwtTokenUtil jwtTokenUtil, UsersDetailsService usersDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.usersDetailsService = usersDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        // String authHeader = request.getHeader("Authorization");
        String token = jwtTokenUtil.extractJwtFromRequest(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // String jwt = authHeader.substring(7);
            try {
                String username = jwtTokenUtil.extractEmail(token);

                if (username != null) {
                    UserDetails userDetails = usersDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }   
            } catch (Exception e) {
                System.err.println("JWT validation failed: " + e.getMessage());
            }
            
        }

        filterChain.doFilter(request, response);
    }
}
