package com.example.zuul.web;

import com.example.zuul.service.RefreshRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiController {
    @Autowired
    private RefreshRouteService refreshRouteService;
    @Autowired
    private ZuulHandlerMapping zuulHandlerMapping;

    @RequestMapping("api/refresh")
    public String refresh() {
        refreshRouteService.refreshRoute();
        return "success";
    }

    @RequestMapping("api/selectRouteInfo")
    @ResponseBody
    public Map<String, Object> selectRouteInfo() {
        return zuulHandlerMapping.getHandlerMap();
    }
}
