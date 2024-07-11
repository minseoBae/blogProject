package com.example.blogproject.config;

import com.example.blogproject.filter.AuthenticationFilter;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final JwtTokenizer jwtTokenizer;
    private final RoleRepository roleRepository;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(jwtTokenizer, roleRepository);
        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // 필터 실행 순서 설정 (낮을수록 먼저 실행)
        return registrationBean;
    }
}