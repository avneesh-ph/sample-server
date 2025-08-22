package com.pharynxai.julius.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pharynxai.julius.server.security.JwtAuthFilter;
import com.pharynxai.julius.server.security.UsersDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UsersDetailsService usersDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    
    public WebSecurityConfig(UsersDetailsService usersDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.usersDetailsService = usersDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
			.csrf(csrf -> csrf.disable()) // disable CSRF for APIs
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/signup", "/auth/signin").permitAll() // allow signup + login
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT is stateless
        .httpBasic(Customizer.withDefaults()); // optional: for testing APIs with basic auth

        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(usersDetailsService)
            .passwordEncoder(passwordEncoder())
            .and().build();
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
