package com.viktor.chatApplication.config;

import com.viktor.chatApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.viktor.chatApplication.enums.Roles.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables use of @PreAuthorize
public class AppSecurityConfig {
    private final AppPasswordConfig appPasswordConfig;
    private final UserService userService;

    @Autowired
    public AppSecurityConfig(AppPasswordConfig appPasswordConfig, UserService userService) {
        this.appPasswordConfig = appPasswordConfig;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/admin-page").hasRole(ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()) // springs egna login page
                .authenticationProvider(daoAuthenticationProvider()) // Tell spring to use our
                .build();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(appPasswordConfig.bCryptPasswordEncoder());
        provider.setUserDetailsService(userService);

        return provider;
    }
}