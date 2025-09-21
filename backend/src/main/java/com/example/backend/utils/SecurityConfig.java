package com.example.backend.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .authorizeHttpRequests( request -> request
                        .requestMatchers("/itb-mshop/v1/**").permitAll()
                        .requestMatchers("/itb-mshop/v2/**").permitAll()
                        .requestMatchers("/itb-mshop/api/files/**").permitAll()

                        .requestMatchers(HttpMethod.POST,"/itb-mshop/v2/sale-items").hasAnyAuthority("SELLER")
                        .anyRequest().authenticated()
                )

	            .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager ( AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}


