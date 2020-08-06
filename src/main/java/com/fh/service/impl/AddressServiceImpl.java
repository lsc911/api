package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.mapper.AddressMapper;
import com.fh.model.po.Address;
import com.fh.model.vo.AddressInfo;
import com.fh.service.AddressService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public List<AddressInfo> queryAddress() {
        Map user = (Map) request.getAttribute("login_user");
        String iphone = (String) user.get("iphone");

        QueryWrapper qw = new QueryWrapper();
        qw.eq("vipId", iphone);

        //查询收数据库的数据
        List<Address> addresses = addressMapper.selectList(qw);


        List<AddressInfo> list = new ArrayList<>();
        //处理真正想要的数据
        for (int i = 0; i < addresses.size(); i++) {
            AddressInfo temp = new AddressInfo();
            //
            Address address = addresses.get(i);
            temp.setId(address.getId());
            temp.setIphone(address.getIphone());
            temp.setName(address.getName());


            String areaIds = address.getAreaIds();
            String areaName = RedisUse.getAreaNames(areaIds);

            temp.setAddress(areaName + address.getDetailAdd());
            temp.setIsCheck(address.getIsCheck());
            list.add(temp);
        }

       return list;
    }


    @Override
    public void addAddress(Address address) {
        addressMapper.addAddress(address);
    }
}
