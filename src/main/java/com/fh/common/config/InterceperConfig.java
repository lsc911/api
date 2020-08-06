package com.fh.common.config;

import com.fh.common.intercepter.CrossIntercepter;
import com.fh.common.intercepter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//拦截器的配置   拦截那些东西  不拦截那些东西
@Configuration //声明是配置文件类
public class InterceperConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册跨域拦截器
        InterceptorRegistration crossInterceptor = registry.addInterceptor(new CrossIntercepter());
        crossInterceptor.addPathPatterns("/**");                      //所有路径都被拦截//注册跨域拦截器


        //登录拦截器
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new LoginInterceptor());
        loginInterceptor.addPathPatterns("/CartController/**","/OrderController/**","/AddressController/**");
    }



}
