package com.fh.controller;

import com.fh.model.po.Area;
import com.fh.service.AreaService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(description = "这是地区类")
@Controller
@RequestMapping("AreaController")
public class AreaController {


    @Autowired
    private AreaService areaService;

    @ApiOperation("测试方法")
    @ApiResponses({
            @ApiResponse(code = 404 ,message = "请求路径不对"),
            @ApiResponse(code = 1000 ,message = "用户没有登入")
    })
    @RequestMapping("queryArea")
    @ResponseBody
    public Map queryArea(){
        Map map = new HashMap();
        try {
            List<Area> areas= areaService.queryArea();
            map.put("code",200);
            map.put("data",areas);
            map.put("message","success");
        }catch (Exception e){
            map.put("code",500);
            map.put("message",e.getMessage());
        }
        return map;
    }



}
