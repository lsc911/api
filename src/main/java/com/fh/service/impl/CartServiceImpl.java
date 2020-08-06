package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.mapper.GoodsMapper;
import com.fh.model.po.Goods;
import com.fh.model.vo.GoodsCart;
import com.fh.service.CartService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Integer addGoodsToCart(Integer id, Integer count) {

        //判断库存
        if(count>0){
            Goods goods = goodsMapper.selectById(id);
            if(count>goods.getStoryCount()){//判断库存是否够
                return goods.getStoryCount()-count;
            }
        }

        //将数据存入redis中
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车指定商品信息
        String goodsInfo = RedisUse.hget("cart_" + iphone + "_lsc", id + "");

        //判断商品是否存在购物车
        if(StringUtils.isEmpty(goodsInfo)){
            GoodsCart goodsCart = goodsMapper.queryGoodsCartById(id);
            goodsCart.setCheck(true);
            goodsCart.setCount(count);

            //计算小计
            BigDecimal money = goodsCart.getPrice().multiply(new BigDecimal(count));
            goodsCart.setMoney(money);

            //将商品信息转为json字符串
            String goodsCartString = JSONObject.toJSONString(goodsCart);
            //数据放到redis里
            RedisUse.hset("cart_" + iphone + "_lsc", id + "",goodsCartString);
        }else{
            //将json转为java
            GoodsCart goodsCart = JSONObject.parseObject(goodsInfo, GoodsCart.class);

            //计算个数
            goodsCart.setCount(goodsCart.getCount()+count);

            if(goodsCart.getCount()<=1){
                goodsCart.setCount(1);
            }

            //验证库存是否够
            Goods goods = goodsMapper.selectById(id);
            if(goodsCart.getCount()>goods.getStoryCount()){
                return goods.getStoryCount()-goodsCart.getCount();
            }

            //计算小计
            BigDecimal money = goodsCart.getPrice().multiply(new BigDecimal(goodsCart.getCount()));
            goodsCart.setMoney(money);

            String goodsCartString = JSONObject.toJSONString(goodsCart);
            RedisUse.hset("cart_" + iphone + "_lsc", id + "",goodsCartString);

        }
        long hlen = RedisUse.hlen("cart_" + iphone + "_lsc");
        return (int) hlen;

    }

    @Override
    public List queryCartListByUser() {
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        List<String> goosCart = RedisUse.hvals("cart_" + iphone + "_lsc");
        return goosCart;
    }

    @Override
    public List<GoodsCart> queryCheckGoods() {
        //从redis 取出购物车数据  返回
        //获取登录信息
        //获取用户信息
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车数据
        //获取购物车的所有数据
        List<String> goodsCarts = RedisUse.hvals("cart_" + iphone + "_lsc");

        //实际需求的数据
        List<GoodsCart> list=new ArrayList<>();
        //处理想要的数据
        for (int i = 0; i <goodsCarts.size() ; i++) {
            //购物车里的每一个商品信息
            String goodsCartStr = goodsCarts.get(i);
            //将字符串转为bean
            GoodsCart goodsCart = JSONObject.parseObject(goodsCartStr, GoodsCart.class);
            // 想要的是选中的数据
            if(goodsCart.isCheck()==true){
                list.add(goodsCart);
            }

        }
        return list;
    }


    @Override
    public void updateCartGoodsStatus(String gids) {

        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        List<String> goosCart = RedisUse.hvals("cart_" + iphone + "_lsc");
        for (int i = 0; i <goosCart.size() ; i++) {

            String goodStr = goosCart.get(i);
            GoodsCart goodsCart = JSONObject.parseObject(goodStr, GoodsCart.class);

            // 判断此商品是否为要修改选中状态
            Integer id = goodsCart.getId();
            //判断此商品是否在选中的ids中
            if((","+gids).contains(","+id+",")==true){
                goodsCart.setCheck(true);
                RedisUse.hset("cart_"+iphone+"_lsc",goodsCart.getId()+"",JSONObject.toJSONString(goodsCart));
            }else{
                goodsCart.setCheck(false);
                RedisUse.hset("cart_"+iphone+"_lsc",goodsCart.getId()+"",JSONObject.toJSONString(goodsCart));
            }

        }
    }

}
