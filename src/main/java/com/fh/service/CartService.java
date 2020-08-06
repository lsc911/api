package com.fh.service;


import com.fh.model.vo.GoodsCart;

import java.util.List;

public interface CartService {

    Integer addGoodsToCart(Integer id, Integer count);

    List queryCartListByUser();

    List<GoodsCart> queryCheckGoods();

    void updateCartGoodsStatus(String gids);
}
