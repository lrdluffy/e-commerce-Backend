package com.strawhats.ecommercebackend.security.config;

import com.strawhats.ecommercebackend.model.AppRole;
import com.strawhats.ecommercebackend.model.Role;
import com.strawhats.ecommercebackend.model.User;
import com.strawhats.ecommercebackend.repository.RoleRepository;
import com.strawhats.ecommercebackend.repository.UserRepository;
import com.strawhats.ecommercebackend.security.jwt.AuthEntryPointJwt;
import com.strawhats.ecommercebackend.security.jwt.AuthTokenFilter;
import com.strawhats.ecommercebackend.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Role userRole = roleRepository.findRoleByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(
                            new Role(AppRole.ROLE_USER)
                    ));

            Role sellerRole = roleRepository.findRoleByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> roleRepository.save(
                            new Role(AppRole.ROLE_SELLER)
                    ));

            Role adminRole = roleRepository.findRoleByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(
                            new Role(AppRole.ROLE_ADMIN)
                    ));

            List<Role> adminRoles = Arrays.asList(userRole, adminRole);

            if (!userRepository.existsUserByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder().encode("adminPassword"));
                userRepository.save(admin);
            }

            userRepository.findByUsername("admin")
                    .ifPresent(admin -> {
                        admin.setRoles(adminRoles);
                        userRepository.save(admin);
                    });
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(
                httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()
        ).exceptionHandling(
                httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authEntryPointJwt)
        ).authenticationProvider(
                authenticationProvider()
        ).authorizeHttpRequests(
                authorizeRequests ->
                        authorizeRequests.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/tests/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().permitAll()
//                                .anyRequest().authenticated()
        ).sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ).headers(
                httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.sameOrigin()
                )
        ).addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers("/v2/api-docs",
                        "configuration/ui",
                        "swagger-resources/**",
                        "swagger-ui.html",
                        "configuration/security",
                        "webjars/**"));
    }
}
