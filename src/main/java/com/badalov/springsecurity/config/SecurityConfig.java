package com.badalov.springsecurity.config;

import com.badalov.springsecurity.security.JwtConfigure;
import com.badalov.springsecurity.security.JwtTokenFilter;
import com.badalov.springsecurity.service.Impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtConfigure jwtConfigure;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, JwtConfigure jwtConfigure, JwtTokenFilter jwtTokenFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtConfigure = jwtConfigure;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean(name = "globalFilter")
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/uploadFile").permitAll()
                .requestMatchers("/updateFile").permitAll()
                .requestMatchers("/get").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users").hasRole("ADMIN")
                .requestMatchers("/admin").hasRole("ADMIN")
//                .requestMatchers("/uploadFile").hasAnyRole("ADMIN", "USER")
                .anyRequest()
                .authenticated()
                .and();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
