package com.noname.forum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration 
@EnableWebSecurity 

public class SecurityConfig {
 
    private final JwtFilter jwtFilter;

    SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((request) -> request
                .requestMatchers("/api/auth/token", "/api/auth/login", "/register", "/posts").permitAll()
                .anyRequest().authenticated()
        )
        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // AuthenticationManager authenticationManager(
    //     HttpSecurity http,
    //     SecurityService SecurityService,
    //     PasswordEncoder passwordEncoder
    // ) throws Exception {
    //     AuthenticationManagerBuilder authenticationManagerBuilder =
    //         http.getSharedObject(AuthenticationManagerBuilder.class);
    //     authenticationManagerBuilder
    //         .userDetailsService(SecurityService)
    //         .passwordEncoder(passwordEncoder);
    //     return authenticationManagerBuilder.build();
    // }

    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
