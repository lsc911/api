package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Goods;
import com.fh.model.vo.GoodsCart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//说明他是持久层
public interface GoodsMapper extends BaseMapper<Goods> {



    List<Goods> queryHotGoods(Goods goods);


    List<Goods> queryAllDataById(Goods goods);

    List<Goods> queryAllData(Goods goods);

    GoodsCart queryGoodsCartById(Integer id);

    int updateGoodsCount(Integer id, Integer count);
}
