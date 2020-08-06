package com.fh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.common.json.JsonData;
import com.fh.model.po.Type;
import com.fh.service.TypeService;
import com.fh.util.RedisUse;
import com.fh.util.RedisUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "这是分类")
@Controller
@RequestMapping("TypeController")
@CrossOrigin("http://localhost:8081")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 查询
     *
     */
    @RequestMapping("queryType")
    @ResponseBody
    public JsonData queryType(){
        String types = RedisUse.get("types");
        return  JsonData.getJsonSuccess(types);
    }


}
