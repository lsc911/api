package com.fh.controller;

import com.fh.common.exception.CountException;
import com.fh.common.json.JsonData;
import com.fh.model.po.Order;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("OrderController")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //生成订单      收货地址  支付方式
    @RequestMapping("queryOrder")
    public JsonData queryOrder(Integer addressId, Integer payType,String flag) throws CountException {

        //处理接口幂等性
        //判断redis中是否存在这个key
        boolean exists = RedisUse.exists(flag);
        if(exists==true){//二次请求
            return JsonData.getJsonError(300,"请求处理中");
        }else{
            RedisUse.set(flag,"",10);
        }

        Map map = orderService.queryOrder(addressId, payType);
        return JsonData.getJsonSuccess(map);
    }

    //生成二维码
    @RequestMapping("queryPayWeChat")
    public JsonData queryPayWeChat(Integer orderId) throws Exception{
        Map moneyPhoto = orderService.queryPayWeChat(orderId);
        return  JsonData.getJsonSuccess(moneyPhoto);
    }


    //查询订单状态
    @RequestMapping("queryPayStatus")
    public JsonData queryPayStatus(Integer orderId) throws Exception {
        // 查询支付状态
        Integer status = orderService.queryPayStatus(orderId);
        return JsonData.getJsonSuccess(status);
    }


    @RequestMapping("queryMyOrder")
    public JsonData queryMyOrder(HttpServletRequest request){
        Map user = (Map) request.getAttribute("login_user");
        Integer id = (Integer) user.get("id");
        List<Order> list = orderService.queryMyOrder(id);
        return JsonData.getJsonSuccess(list);
    }


}
