package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

//订单服务
public interface OrderService {
    // 使用1.通过前端url上传过来秒杀活动Id 然后下单接口校验对应Id是否属于对应商品且活动已开始（扩展性更好，不同的下单渠道？）
    //2.直接在下单接口内判断是否是秒杀活动商品，若存在进行中的则以秒杀价格下单
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
