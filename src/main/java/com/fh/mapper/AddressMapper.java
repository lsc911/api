package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMapper extends BaseMapper<Address> {
    //List<Address> queryAddress();

    void addAddress(Address address);
}
