package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.OrderGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {

    public void batchAdd(@Param("list") List<OrderGoods> list, @Param("oid") Integer oid);
}
