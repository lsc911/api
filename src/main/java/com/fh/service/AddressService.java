package com.fh.service;

import com.fh.model.po.Address;
import com.fh.model.vo.AddressInfo;

import java.util.List;

public interface AddressService {
    List<AddressInfo> queryAddress();

    void addAddress(Address address);
}
