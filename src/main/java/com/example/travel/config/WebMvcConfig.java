package com.example.travel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> list1=new ArrayList<>();
        list1.add("/spot/**");
        list1.add("/pdf/**");
        list1.add("/route/**");
        List<String> list2=new ArrayList<>();
        list2.add("/spot/overview/**");
        list2.add("/route/overview/**");
        registry.addInterceptor(interceptor).addPathPatterns(list1).excludePathPatterns(list2);
    }

}


