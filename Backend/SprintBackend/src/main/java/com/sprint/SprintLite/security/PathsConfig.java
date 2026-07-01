package com.sprint.SprintLite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {
    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator/**",
                "/todos/**",
                "/csrf-token",
                "/login",
                "/attachment/**",
                "/forgot-password",
                "/system/status",
                "/bootstrap-admin"
                );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/backlog/**",
                "/feature/**",
                "/product/**",
                "/sprint/**",
                "/story/**",
                "/task/**",
                "/DSU/**",
                "/Bug/**",
                "/profile/**"
        );
    }

    @Bean(name = "adminPaths")
    public List<String> adminPaths() {
        return List.of(
                "/register"
        );
    }
}
