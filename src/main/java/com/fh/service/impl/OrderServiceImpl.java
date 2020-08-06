package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.enums.PayStatusEnum;
import com.fh.common.exception.CountException;
import com.fh.mapper.GoodsMapper;
import com.fh.mapper.OrderGoodsMapper;
import com.fh.mapper.OrderMapper;
import com.fh.model.po.Goods;
import com.fh.model.po.Order;
import com.fh.model.po.OrderGoods;
import com.fh.model.vo.GoodsCart;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import github.wxpay.sdk.FeiConfig;
import github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    public Map queryOrder(Integer addressId, Integer payType) throws CountException {

        //此次请求返回的数据
        Map map = new HashMap();
        //订单详情表
        List<OrderGoods> list = new ArrayList<>();
        //保存数据库
        Order order =new Order();
        order.setAddressId(addressId);
        order.setPayType(payType);
        order.setCreateDate(new Date());
        order.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());

        //设置商品的清单个数
        Integer typeCount =0;

        //设置总金额
        BigDecimal totalMoney= new BigDecimal(0);

        //获取购物车的key
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车数据
        //获取购物车的所有数据
        List<String> goodsStr = RedisUse.hvals("cart_" + iphone + "_lsc");
        for (int i = 0; i <goodsStr.size() ; i++) {

            //将字符串转换为javabean
            GoodsCart goodsCart = JSONObject.parseObject(goodsStr.get(i), GoodsCart.class);

            //判断是否为订单中的商品
            if(goodsCart.isCheck()==true){
                //查询数据库的信息  库存是否足够
                Goods goods = goodsMapper.selectById(goodsCart.getId());
                //库存足够
                if(goods.getStoryCount()>=goodsCart.getCount()){

                    typeCount++;
                    totalMoney = totalMoney.add(goodsCart.getMoney());
                    //维护订单详情表
                    OrderGoods orderGoods = new OrderGoods();
                    orderGoods.setCount(goodsCart.getCount());
                    orderGoods.setGoodsId(goodsCart.getId());
                    list.add(orderGoods);

                    //第二减库存  //更新语句
                    //减库存  数据库的锁 保证不会超卖  update  返回一个值 int
                    int i1 = goodsMapper.updateGoodsCount(goods.getId(), goodsCart.getCount());
                    if(i1==0){
                        throw  new CountException("商品编号为："+goodsCart.getId()+"的库存不足 库存只有："+goods.getStoryCount());
                    }
                }else{
                    throw new CountException("商品编号为:"+goodsCart.getId()+"的库存不足   库存只有："+goods.getStoryCount());
                }
            }
        }
        order.setProTypeCount(typeCount);
        order.setTotalMoney(totalMoney);
        orderMapper.insert(order);
        //保存订单详情表
        orderGoodsMapper.batchAdd(list,order.getId());
        //删除redis的数据   把购物车中已经结算的商品 移除redis
        for (int i = 0; i <goodsStr.size() ; i++) {
            //将字符串转换为javabean
            GoodsCart goodsCart = JSONObject.parseObject(goodsStr.get(i), GoodsCart.class);
            if(goodsCart.isCheck()==true){
                RedisUse.hdel("cart_" + iphone + "_lsc",goodsCart.getId()+"");
            }
        }
        map.put("code",200);
        map.put("orderId",order.getId());
        map.put("totalMoney",totalMoney);
        return map;
    }



    @Override
    public Map queryPayWeChat(Integer orderId) throws Exception{

        Map map = new HashMap();
        //从redsi中判断是否已经生成过
        String moneyPhotoUrl = RedisUse.get("order_" + orderId + "_lsc");

        if(StringUtils.isEmpty(moneyPhotoUrl)!=true){//不为空，已经生成二维码
            map.put("code",200);
            map.put("url",moneyPhotoUrl);
            return map;
        }
        Order order = orderMapper.selectById(orderId);
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商666-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        //String mill = "weixin1_order_"+orderId+System.currentTimeMillis();
        data.put("out_trade_no","weixin1_order_lsc_"+orderId);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);

        //设置订单金额.   单位分
        // data.put("total_fee",moneyAll.toString());
        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        //resp.put("mill",mill);
        System.out.println(orderId+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            map.put("code",200);
            map.put("url",resp.get("code_url"));
            //更新订单状态
            order.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
            orderMapper.updateById(order);
            //将二维码存入redis  设置失效时间
            RedisUse.set("order_"+orderId+"_lsc",resp.get("code_url"),30*60);
        }else {
            map.put("code",600);
            map.put("info",resp.get("return_msg"));
        }
        //System.out.println(resp);
        return map;
    }

    //
    @Override
    public Integer queryPayStatus(Integer orderId) throws Exception{
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();

        data.put("out_trade_no","weixin1_order_lsc_"+orderId);
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询结果"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新订单状态
                Order order = new Order();
                order.setId(orderId);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_SUCCESS.getStatus());
                orderMapper.updateById(order);
                return  1;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }
        }
        return 0;
    }

    @Override
    public List<Order> queryMyOrder(Integer id) {

        return orderMapper.queryMyOrder(id);
    }


}
