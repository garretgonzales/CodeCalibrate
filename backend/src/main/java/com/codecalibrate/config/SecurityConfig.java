package com.codecalibrate.config;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
               .cors(Customizer.withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(
                    (request, response, exception) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/api/auth/register")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/login")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET, "/api/learning-paths", "/api/learning-paths/**")
                    .permitAll()
                    .requestMatchers(regexMatcher(HttpMethod.GET, "^/api/exercises/\\d+$"))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .build();


  }
}