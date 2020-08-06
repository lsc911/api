package com.fh.service.impl;

import com.fh.mapper.UserMapper;
import com.fh.model.po.Vip;
import com.fh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Vip queryVipByIphone(String iphone) {
        return userMapper.queryVipByIphone(iphone);
    }

    @Override
    public Integer addVip(Vip vip) {
        userMapper.addVip(vip);
        return vip.getId();
    }
}
