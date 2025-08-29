package com.pharynxai.julius.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
			.csrf(csrf -> csrf.disable()) // disable CSRF for APIs
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui.html/**", "/swagger-ui/**", "/auth/signup", "/auth/signin").permitAll() // allow signup + login
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT is stateless
        .httpBasic(Customizer.withDefaults()); // optional: for testing APIs with basic auth

        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UsersDetailsService usersDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(usersDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
