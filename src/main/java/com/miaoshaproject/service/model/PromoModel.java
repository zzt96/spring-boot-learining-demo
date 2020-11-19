package com.miaoshaproject.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;


public class PromoModel {
    private Integer id;

    //秒杀活动状态 1表示未开始 2进行中 3结束
    private  Integer status;
    //秒杀活动名称
    private String promoName;

    //秒杀活动的开始时间
    private DateTime startTime;

    //秒杀活动的结束时间
    private DateTime endTime;

    //秒杀活动的适用商品
    private Integer itemId;

    //秒杀活动的价格
    private BigDecimal promoItemPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
