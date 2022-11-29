package com.example.ribbon.web;

import com.example.ribbon.service.RibbonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RibbonController {
    @Resource
    RibbonService ribbonService;

    @RequestMapping(value = "/api/selectUserInfo", method = RequestMethod.GET)
    public String selectUserInfo(@RequestParam String userId) {
        return ribbonService.selectUserInfo(userId) + " From Ribbon";
    }
}
