package com.example.zuul.config;

import com.example.zuul.filter.TokenFilter;
import com.example.zuul.router.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {
    @Autowired
    private ZuulProperties zuulProperties;
    @Autowired
    private ServerProperties serverProperties;

    @Bean
    public RouteLocator routeLocator() {
        return new RouteLocator(serverProperties.getServlet().getPath(), zuulProperties);
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
