package org.interview.projectinterview.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;

    protected static String [] publicAPIList = {
      "/api/v1/auth/login",
      "/api/v1/users/register",
    };

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }


  @Bean SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
    httpSecurity.csrf(AbstractHttpConfigurer::disable)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .authorizeHttpRequests(auth -> auth.requestMatchers(publicAPIList).permitAll()
        .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(customJwtDecoder)
            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            .authenticationEntryPoint(new AuthenticationEntryPoint()))
        .exceptionHandling(exceptionHandle -> exceptionHandle.accessDeniedHandler(new JwtAccessDeniedHandle()));
      return httpSecurity.build();
    }
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
      JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
      jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "x-no-retry", "Access-Control-Allow-Origin"));
    corsConfiguration.setExposedHeaders(List.of("Authorization", "Content-Type"));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
