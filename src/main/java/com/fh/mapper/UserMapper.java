package com.fh.mapper;

import com.fh.model.po.Vip;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    Vip queryVipByIphone(String iphone);

    void addVip(Vip vip);
}
