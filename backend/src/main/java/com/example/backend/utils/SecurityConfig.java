package com.example.backend.utils;

import com.example.backend.filters.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration

public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) 
//                .authorizeHttpRequests( (auth) -> auth.anyRequest().permitAll())
                .authorizeHttpRequests((auth) -> auth
                        // อนุญาตให้ Endpoints ที่เกี่ยวข้องกับการยืนยันตัวตนเข้าถึงได้
                        .requestMatchers(HttpMethod.POST, "/v2/auth/**").permitAll()
                        // PBI 25 และ Endpoints ที่เกี่ยวข้องกับการดูสินค้าสำหรับผู้ซื้อ
                        .requestMatchers(HttpMethod.GET, "/v2/sale-items/**", "/v2/brands/**").permitAll()
                        // คำขอที่เหลือทั้งหมดต้องผ่านการยืนยันตัวตน (Authenticated)
                        .anyRequest().authenticated()
                )
//                .cors(withDefaults())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);;
        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
//        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}