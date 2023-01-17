package com.badalov.springsecurity.config;

import com.badalov.springsecurity.annotation.CustomUserIdResolver;
import com.badalov.springsecurity.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class CustomUserIdResolverConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomUserIdResolverConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomUserIdResolver(jwtTokenProvider));
    }
}
