package com.example.feign.service.hystrix;

import com.example.feign.service.FeignService;
import org.springframework.stereotype.Component;

@Component
public class FeignServiceHystrix implements FeignService {
    @Override
    public String selectUserInfo(String userId) {
        return "selectUserInfo By userId:" + userId + " from Feign Hystrix";
    }
}
