package com.example.blogproject.config;

import com.example.blogproject.filter.AuthenticationFilter;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/temp/upload/");
        registry.addResourceHandler("/summernote_image/**")
                .addResourceLocations("file:///C:/temp/summernote_image/");
    }
}