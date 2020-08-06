package com.fh.controller;


import com.fh.common.json.JsonData;
import com.fh.model.po.Vip;
import com.fh.service.UserService;
import com.fh.util.OssUtil;
import com.fh.util.RedisUse;
import com.fh.util.RedisUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Api(description = "这是VIP类")
@RestController
@RequestMapping("UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("findAreaList")
    public JsonData findAreaList(){
        String areaList = RedisUse.get("areaList");
        return JsonData.getJsonSuccess(areaList);
    }

    @RequestMapping("addVip")
    public JsonData addVip(Vip vip){
        Integer id = userService.addVip(vip);
        RedisUtil.hsetRedis("vip_lsc",id.toString(),vip);
        return JsonData.getJsonSuccess("success");
    }

    @RequestMapping("OSSuploadFile")
    public Map OSSuploadFile(@RequestParam("img") MultipartFile img){

        Map map = new HashMap();

        try {
            File file = OssUtil.readFiles(img);
            String filePath = OssUtil.uploadFile(file);
            map.put("filePath",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
}
