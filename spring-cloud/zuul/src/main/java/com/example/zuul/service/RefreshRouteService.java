package com.example.zuul.service;

import com.example.zuul.router.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RefreshRouteService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteLocator routeLocator;

    /**
     * 路由刷新服务
     */
    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }
}
