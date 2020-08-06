package com.fh.controller;

import com.fh.common.json.JsonData;
import com.fh.model.vo.GoodsCart;
import com.fh.service.CartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "这是购物车类")
@RestController
@RequestMapping("CartController")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("addCart")
    public JsonData addCart(@RequestParam("id") Integer id, Integer count){
        Integer count_type = cartService.addGoodsToCart(id,count);
        return JsonData.getJsonSuccess(count_type);
    }

    //查出所有商品
    @RequestMapping("queryCartListByUser")
    public JsonData queryCartListByUser(){
        List list =  cartService.queryCartListByUser();
        return JsonData.getJsonSuccess(list);
    }


    //查询购物车中要结算的商品信息
    //返回的数据格式 [{"id":id,}]
    @RequestMapping("queryCheckGoods")
    @ResponseBody
    public JsonData queryCheckGoods(){
        List<GoodsCart> goodsCarts = cartService.queryCheckGoods();
        return JsonData.getJsonSuccess(goodsCarts);
    }


    //更新购物车中商品的状态
    @RequestMapping("updateCartGoodsStatus")
    @ResponseBody
    public JsonData updateCartGoodsStatus(String gids){
        cartService.updateCartGoodsStatus(gids);
        return JsonData.getJsonSuccess("修改成功");
    }

}
