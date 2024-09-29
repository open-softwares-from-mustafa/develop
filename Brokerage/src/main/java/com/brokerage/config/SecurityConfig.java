package com.brokerage.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity

    @Configuration
    public class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/customers/**", "/orders/**").authenticated()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
            http.headers().frameOptions().sameOrigin();
            return http.build();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withUsername("admin")
                    .password("{noop}admin@2024")
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }
    }