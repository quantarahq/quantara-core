package dev.quantara.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * MVP security posture: all API endpoints are open, and CSRF is disabled since this is a
 * stateless JSON API with no browser session. There is no authenticated-user concept yet —
 * that is intentionally out of scope for the MVP (see spec's "Out of Scope" section) and is
 * the seam where real authN/authZ would be added in a future iteration.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
