package com.fh.controller;

import com.fh.common.json.JsonData;
import com.fh.model.po.Goods;
import com.fh.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "这是商品类")
@RestController
@RequestMapping("GoodsController")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    //热销
    @RequestMapping("queryHotGoods")
    public JsonData queryHotGoods(Goods goods){

        List<Goods> list = goodsService.queryHotGoods(goods);
        return JsonData.getJsonSuccess(list);
    }


    //根据商品id查询商品
    @RequestMapping("queryAllDataById")
    public JsonData queryAllDataById(Goods goods){

        List<Goods> list = goodsService.queryAllDataById(goods);
        return JsonData.getJsonSuccess(list);

    }


    @RequestMapping("queryAllData")
    public JsonData queryAllData(Goods goods){
        if(goods.getTypeId().equalsIgnoreCase(",undefined,")){
            goods.setTypeId(null);
        }
        List<Goods> goodsList = goodsService.queryAllData(goods);
        return  JsonData.getJsonSuccess(goodsList);
    }


}