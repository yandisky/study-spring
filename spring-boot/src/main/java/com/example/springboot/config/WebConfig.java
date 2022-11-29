package com.example.springboot.config;

import com.example.springboot.interceptor.LogInterceptor;
import com.example.springboot.interceptor.OldLoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
        registry.addInterceptor(new OldLoginInterceptor()).addPathPatterns("/admin/oldLogin");
    }
}
