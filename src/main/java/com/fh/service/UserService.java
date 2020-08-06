package com.fh.service;

import com.fh.model.po.Vip;

public interface UserService {
    Vip queryVipByIphone(String iphone);

    Integer addVip(Vip vip);
}
