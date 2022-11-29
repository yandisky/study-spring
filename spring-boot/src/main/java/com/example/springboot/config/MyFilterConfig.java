package com.example.springboot.config;

import com.example.springboot.filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class MyFilterConfig {
    @Autowired
    MyFilter myFilter;

    @Bean
    public FilterRegistrationBean<MyFilter> setUpMyFilter() {
        FilterRegistrationBean<MyFilter> myFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        myFilterFilterRegistrationBean.setOrder(1);
        myFilterFilterRegistrationBean.setFilter(myFilter);
        myFilterFilterRegistrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/")));
        return myFilterFilterRegistrationBean;
    }
}
