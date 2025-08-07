package org.example.ReviewServiceGP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

        /**
         * Defines the security filter chain for HTTP requests.
         *
         * All endpoints require authentication. The application is configured
         * to act as an OAuth2 Resource Server, expecting a JWT in the
         * Authorization header of incoming requests.
         *
         * Tokens are validated against the issuer configured in
         * { application.properties} or { application.yml}, using the
         * { spring.security.oauth2.resourceserver.jwt.issuer-uri} property.
         *
         * @param http the {@link HttpSecurity} object used to configure HTTP security
         * @return the configured {@link SecurityFilterChain}
         * @throws Exception if an error occurs while building the filter chain
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    // Secure All Requests
                    .authorizeHttpRequests(
                            auth -> auth
                                    .anyRequest().authenticated() // means every single http request to this service must include a valid JWT.
                    )
                    // Configure this service as oauth2 resource server
                    .oauth2ResourceServer(
                            oauth2 -> oauth2
                                    .jwt(
                                            // you should be logged in for every http request
                                            // it expects JWT token in requests like:
                                            // Authorization: Bearer <token>
                                            jwtConfigurer -> {
                                                // We ask the JWT issuer to verify it.
                                                // Leave empty if using default decoder (from issuer-uri)
                                            }
                                    )
                    );
            // Finalise and return the filter chain
            return http.build(); // returns SecurityFilterChain object and registers it with Spring.
        }
    }
