package com.fh.service.impl;

import com.fh.mapper.AreaMapper;
import com.fh.model.po.Area;
import com.fh.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;


    @Override
    public List<Area> queryArea() {
        List<Area> areas = areaMapper.selectList(null);
        return areas;
    }

    

}
