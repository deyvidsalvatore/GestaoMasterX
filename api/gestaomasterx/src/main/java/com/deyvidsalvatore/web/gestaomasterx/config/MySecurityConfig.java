package com.deyvidsalvatore.web.gestaomasterx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() { 
    	return new BCryptPasswordEncoder(); 
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests -> {
                configurePermissions(authorizeRequests);
                authorizeRequests
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("/auth/logout").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated();
            })
            .formLogin(formLogin -> formLogin
                .successHandler((req, res, auth) -> {
                    res.setContentType("application/json");
                    res.getWriter().write("{\"message\": \"Login successful\"}");
                })
                .failureHandler((req, res, exception) -> res.setStatus(HttpStatus.UNAUTHORIZED.value()))
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setContentType("application/json");
                    res.setStatus(HttpStatus.OK.value());
                    res.getWriter().write("{\"message\": \"Logout successful\"}");
                })
                .permitAll()
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .expiredUrl("/auth/login")
            );

        return http.build();
    }

    private void configurePermissions(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests) {
        String rolePrefix = "ROLE_";

        requests.requestMatchers(HttpMethod.GET, "/api/v1/administrativo/**")
            .hasAuthority(rolePrefix + "ADMINISTRADOR")
            .requestMatchers(HttpMethod.POST, "/api/v1/administrativo/**")
            .hasAuthority(rolePrefix + "ADMINISTRADOR")
            .requestMatchers(HttpMethod.PUT, "/api/v1/administrativo/**")
            .hasAuthority(rolePrefix + "ADMINISTRADOR")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/administrativo/**")
            .hasAuthority(rolePrefix + "ADMINISTRADOR");
        
        /* Horas totais */
        requests.requestMatchers(HttpMethod.GET, "/api/v1/funcionarios/{funcionarioId}/horas/totais")
        .hasAuthority(rolePrefix + "GESTOR")
        .requestMatchers(HttpMethod.GET, "/api/v1/funcionarios/{funcionarioId}/horas/totais")
        .hasAuthority(rolePrefix + "FUNCIONARIO");
        
        requests.requestMatchers(HttpMethod.GET, "/api/v1/gestores/**")
            .hasAuthority(rolePrefix + "GESTOR")
            .requestMatchers(HttpMethod.POST, "/api/v1/gestores/**")
            .hasAuthority(rolePrefix + "GESTOR")
            .requestMatchers(HttpMethod.PUT, "/api/v1/gestores/**")
            .hasAuthority(rolePrefix + "GESTOR")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/gestores/**")
            .hasAuthority(rolePrefix + "GESTOR");

        requests.requestMatchers(HttpMethod.GET, "/api/v1/funcionarios/**")
            .hasAuthority(rolePrefix + "FUNCIONARIO")
            .requestMatchers(HttpMethod.POST, "/api/v1/funcionarios/**")
            .hasAuthority(rolePrefix + "FUNCIONARIO")
            .requestMatchers(HttpMethod.PUT, "/api/v1/funcionarios/**")
            .hasAuthority(rolePrefix + "FUNCIONARIO")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/funcionarios/**")
            .hasAuthority(rolePrefix + "FUNCIONARIO");


    }
}
