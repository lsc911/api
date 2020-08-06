package com.fh.controller;

import com.fh.common.json.JsonData;
import com.fh.model.po.Address;
import com.fh.model.vo.AddressInfo;
import com.fh.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("AddressController")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("queryAddress")
    public JsonData queryAddress(){
        List<AddressInfo> list = addressService.queryAddress();
        return JsonData.getJsonSuccess(list);
    }

    @RequestMapping("addAddress")
    public JsonData addAddress(Address address){
        addressService.addAddress(address);
        return JsonData.getJsonSuccess("添加成功");

    }

}
