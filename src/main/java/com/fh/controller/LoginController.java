package com.fh.controller;

import com.fh.common.json.JsonData;
import com.fh.model.po.Vip;
import com.fh.service.UserService;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Api(description = "这是登录类")
@RestController
@RequestMapping("LoginController")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("sendMessage")
    public JsonData sendMessage(String iphone){
        String code="1111";
        RedisUse.set(iphone+"_lsc",code,60*5);
        return JsonData.getJsonSuccess("短信发送成功");

    }

    @RequestMapping("login")
    public JsonData login(String iphone, String code){

        Map logMap = new HashMap();

        String redis_code = RedisUse.get(iphone + "_lsc");
        if(redis_code!=null&&redis_code.equals(code)){
            Map user = new HashMap();
            Vip vip=userService.queryVipByIphone(iphone);
            user.put("iphone",iphone);
            user.put("id",vip.getId());
            String sign =  JWT.sign(user,1000 * 60 * 60 * 24);
            String token =  Base64.getEncoder().encodeToString((iphone+","+sign).getBytes());
            RedisUse.set("token_"+iphone,sign,60*30);
            logMap.put("status","200");
            logMap.put("message","登陆成功");
            logMap.put("token",token);

        }else{
            logMap.put("status","300");
            logMap.put("message","用户不存在 或者密码错误");
        }
        return JsonData.getJsonSuccess(logMap);
    }

}
