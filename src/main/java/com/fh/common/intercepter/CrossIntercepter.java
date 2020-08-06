package com.fh.common.intercepter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CrossIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置response
        // 允许跨域访问的域名：若有端口需写全（协议+域名+端口），若没有端口末尾不用加'/'
        //response.setHeader("Access-Control-Allow-Origin", "http://www.domain1.com");
        //获取请求的域名
        String yuming = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", yuming);

        //允许session //是否支持cookie跨域
        response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers","x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials","true");
        //当客户端修改了头信息     发起两个请求  第一个是预请求   options （是否允许修改头信息）     发起真正的请求
        String method = request.getMethod();
        System.out.println(method);
        //
        if(method.equalsIgnoreCase("options")){
            //允许修改头信息  添加一个name属性
            response.setHeader("Access-Control-Allow-Headers","token");
            return false;
        }
        //从header 里去数据
        String name = request.getHeader("token");
        System.out.println(name);

        return true;
    }
}
