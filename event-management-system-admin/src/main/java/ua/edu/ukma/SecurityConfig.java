package ua.edu.ukma;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**")
                            .permitAll()
                        .requestMatchers("/instances/**")
                            .permitAll()
                        .requestMatchers("/health/**")
                            .permitAll()
                        .requestMatchers("/**")    // For user/admin panel
                            .authenticated()
                        .anyRequest()
                            .denyAll()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
