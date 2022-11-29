package com.example.zuul.router;

import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private ZuulProperties zuulProperties;

    public RouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        zuulProperties = properties;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routeLinkedHashMap = new LinkedHashMap<>();
        // 从application.properties中加载路由信息
        routeLinkedHashMap.putAll(super.locateRoutes());
        // 从自定义路由配置组中加载路由信息
        routeLinkedHashMap.putAll(routesConfigGroup());
        // 整理配置信息
        LinkedHashMap<String, ZuulRoute> zuulRouteLinkedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routeLinkedHashMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(zuulProperties.getPrefix())) {
                path = zuulProperties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            zuulRouteLinkedHashMap.put(path, entry.getValue());
        }
        return zuulRouteLinkedHashMap;
    }

    /**
     * 自定义路由配置组
     *
     * @return 配置组内容
     */
    private Map<String, ZuulRoute> routesConfigGroup() {
        Map<String, ZuulRoute> routeMap = new LinkedHashMap<>();

        ZuulRoute zuulRoute = new ZuulRoute();
        zuulRoute.setId("route-a");
        zuulRoute.setPath("/route-a/**");
        zuulRoute.setServiceId("demo-spring-cloud-feign");
        zuulRoute.setRetryable(false);
        zuulRoute.setStripPrefix(true);
        zuulRoute.setSensitiveHeaders(new HashSet<>());
        routeMap.put(zuulRoute.getPath(), zuulRoute);

        return routeMap;
    }
}
