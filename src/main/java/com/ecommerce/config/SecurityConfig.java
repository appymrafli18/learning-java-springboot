package com.ecommerce.config;

import com.ecommerce.exceptions.CustomAccessDeniedHandler;
import com.ecommerce.exceptions.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // wajib untuk akses method yang di proteksi oleh security (authorization)
public class SecurityConfig {

//  private final JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    http
      .csrf(AbstractHttpConfigurer::disable) // mematikan csrf protection
      .formLogin(AbstractHttpConfigurer::disable) // mematikan form login
      .authorizeHttpRequests(
        auth -> auth.requestMatchers("/api/auth/**").permitAll()
          .requestMatchers("/api/categories/**").permitAll()
          .requestMatchers("/api/product/**").hasRole("USER")
          .requestMatchers("/api/user/**").hasAnyRole("ADMIN") //banyak role bisa mengakses endpoint ini
          .anyRequest().authenticated()
      )
      .exceptionHandling(ex ->
        ex
          .accessDeniedHandler(accessDeniedHandler)
//          .authenticationEntryPoint(authenticationEntryPoint)
      ) // menambahkan custom exception handler
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // mematikan session management
      .httpBasic(AbstractHttpConfigurer::disable); // mematikan basic authentication

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
