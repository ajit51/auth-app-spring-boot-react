package com.auth.auth_app_backend.config;

import com.auth.auth_app_backend.dtos.ApiError;
import com.auth.auth_app_backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {

  private JwtAuthenticationFilter jwtAuthenticationFilter;
  private AuthenticationSuccessHandler successHandler;

  public SecurityConfig(
      AuthenticationSuccessHandler successHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.successHandler = successHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorizeHttpRequest ->
                authorizeHttpRequest
                    .requestMatchers(AppConstant.AUTH_PUBLIC_URL)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(oauth2 -> oauth2.successHandler(successHandler).failureHandler(null))
        .logout(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            e ->
                e.authenticationEntryPoint(
                    ((request, response, ex) -> {
                      // error message
                      response.setStatus(401);
                      response.setContentType("application/json");
                      String message = "Unauthorized Access !!" + ex.getMessage();
                      String error = (String) request.getAttribute("error");
                      if (error != null) {
                        message = error;
                      }
                      //                      Map<String, String> errorMap = Map.of("message",
                      // message, "statusCode", Integer.toString(401));

                      ApiError apiError =
                          ApiError.of(
                              HttpStatus.UNAUTHORIZED.value(),
                              "Unauthorized Access",
                              message,
                              request.getRequestURI());

                      var objectMapper = new ObjectMapper();
                      response.getWriter().write(objectMapper.writeValueAsString(apiError));
                    })))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
