package com.fh.service.impl;

import com.fh.mapper.AreaMapper;
import com.fh.mapper.GoodsMapper;
import com.fh.model.po.Goods;
import com.fh.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private AreaMapper areaMapper;



    @Override
    public List<Goods> queryHotGoods(Goods goods) {
        return  goodsMapper.queryHotGoods(goods);
    }

    @Override
    public List<Goods> queryAllDataById(Goods goods) {
        List<Goods> list = goodsMapper.queryAllDataById(goods);
        for (int i = 0; i <list.size() ; i++) {
            String areaId = list.get(i).getAreaId();
            if(StringUtils.isEmpty(areaId)==false){
                List<String> strings = areaMapper.queryAreaNameById(areaId);

                StringBuffer sb = new StringBuffer();

                for (int j = 0; j <strings.size(); j++) {
                    sb.append(strings.get(j)).append(",");
                }
                list.get(i).setAreaId(sb.toString().substring(0,sb.lastIndexOf(",")));
            }
        }
        return list;
    }

    @Override
    public List<Goods> queryAllData(Goods goods) {
        return goodsMapper.queryAllData(goods);
    }
}
