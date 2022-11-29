package com.example.springboot.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "MyFilterWithAnnotation", urlPatterns = "/admin/*")
public class MyFilterWithAnnotation implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFilterWithAnnotation.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("MyFilterWithAnnotation init:{}", filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("MyFilterWithAnnotation doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestUri = httpServletRequest.getRequestURI();
        LOGGER.info("MyFilterWithAnnotation request api:{}", requestUri);
        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        Long endTime = System.currentTimeMillis();
        LOGGER.info("MyFilterWithAnnotation response time:{}", (endTime - startTime));
    }

    @Override
    public void destroy() {
        LOGGER.info("MyFilterWithAnnotation destroy");
    }
}
