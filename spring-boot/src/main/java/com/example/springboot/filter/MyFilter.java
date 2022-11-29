package com.example.springboot.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MyFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("MyFilter init:{}", filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("MyFilter doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestUri = httpServletRequest.getRequestURI();
        LOGGER.info("MyFilter request api:{}", requestUri);
        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        Long endTime = System.currentTimeMillis();
        LOGGER.info("MyFilter response time:{}", (endTime - startTime));
    }

    @Override
    public void destroy() {
        LOGGER.info("MyFilter destroy");
    }
}
