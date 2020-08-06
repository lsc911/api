package com.fh.service;

import com.fh.common.exception.CountException;
import com.fh.model.po.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map queryOrder(Integer addressId, Integer payType) throws CountException;

    Map queryPayWeChat(Integer orderId) throws Exception;

    Integer queryPayStatus(Integer orderId) throws Exception;

    List<Order> queryMyOrder(Integer id);
}
