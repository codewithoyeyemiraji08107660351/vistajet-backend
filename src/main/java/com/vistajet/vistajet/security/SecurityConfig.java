package com.vistajet.vistajet.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/leadership/leaders").permitAll()
                        .requestMatchers("/api/v1/leadership/leader/find/**").permitAll()
                        .requestMatchers("/api/v1/news/all-news").permitAll()
                        .requestMatchers("/api/v1/news/find/**").permitAll()
                        .requestMatchers("/api/v1/testimonials/create-testimonials","/api/v1/testimonials/all-testimonials", "/api/v1/testimonials/find/**").permitAll()
                        .requestMatchers("/api/v1/partners/all-partners", "/api/v1/partners/find/**").permitAll()
                        .requestMatchers("/api/v1/contact/add-contact").permitAll()
                        .requestMatchers("/api/v1/about/all-about").permitAll()
                        .requestMatchers("/api/v1/gallery/galleries").permitAll()
                        .requestMatchers("/api/v1/gallery/find/**").permitAll()
                        .requestMatchers("/api/v1/service/all-service").permitAll()
                        .requestMatchers("/api/v1/service/find/**").permitAll()
                        .requestMatchers("/api/v1/leadership/**", "/api/v1/news/**", "/api/v1/gallery/**", "/api/v1/partners/**", "/api/v1/service/**", "/api/v1/service/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
