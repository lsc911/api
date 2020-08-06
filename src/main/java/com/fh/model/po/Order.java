package com.fh.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@TableName("order_order")
public class Order {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("addressId")
    private Integer addressId;

    @TableField("vipId")
    private Integer vipId;

    @TableField("payType")
    private Integer payType;

    @TableField("proTypeCount")
    private Integer proTypeCount;

    @TableField("totalMoney")
    private BigDecimal totalMoney;

    @TableField("payStatus")
    private Integer payStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("createDate")
    private Date createDate;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getProTypeCount() {
        return proTypeCount;
    }

    public void setProTypeCount(Integer proTypeCount) {
        this.proTypeCount = proTypeCount;
    }
}
