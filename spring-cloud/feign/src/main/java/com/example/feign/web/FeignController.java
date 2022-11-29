package com.example.feign.web;

import com.example.feign.service.FeignService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class FeignController {
    @Resource
    FeignService feignService;

    @RequestMapping(value = "/api/selectUserInfo", method = RequestMethod.GET)
    public String selectUserInfo(@RequestParam String userId) {
        return feignService.selectUserInfo(userId) + " From Feign";
    }
}
