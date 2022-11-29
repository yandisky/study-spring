package com.example.feign.service;

import com.example.feign.service.hystrix.FeignServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "demo-spring-cloud-eureka-client", fallback = FeignServiceHystrix.class)
public interface FeignService {
    @RequestMapping(value = "/api/selectUserInfo", method = RequestMethod.GET)
    String selectUserInfo(@RequestParam String userId);
}
