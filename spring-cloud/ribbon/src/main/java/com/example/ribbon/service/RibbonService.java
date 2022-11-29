package com.example.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "selectUserInfoFallback")
    public String selectUserInfo(String userId) {
        return restTemplate.getForObject("http://demo-spring-cloud-eureka-client/api/selectUserInfo?userId=" + userId, String.class);
    }

    private String selectUserInfoFallback(String userId) {
        return "selectUserInfo By userId:" + userId + " from Ribbon Hystrix";
    }
}
