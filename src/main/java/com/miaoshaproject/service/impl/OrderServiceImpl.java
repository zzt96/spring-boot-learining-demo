package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.orderDOMapper;
import com.miaoshaproject.dao.sequenceDOMapper;
import com.miaoshaproject.dataobject.orderDO;
import com.miaoshaproject.dataobject.sequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    /**
     * 创建订单服务
     * @param userId
     * @param itemId
     * @param amount
     * @return
     */
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private sequenceDOMapper sequenceDOMapper;

    @Autowired(required = false)
    private orderDOMapper orderDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //1.校验下单状态 下单商品是否存在 用户是否合法 购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMTER_VALIDATION_ERROR,"商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMTER_VALIDATION_ERROR,"用户不存在");
        }
        if(amount<=0 || amount >= 99){ //购买过多 或 为负值
            throw new BusinessException(EmBusinessError.PARAMTER_VALIDATION_ERROR,"数量信息不正确");
        }

        //2.落单减库存，
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));


        //生成订单号
        orderModel.setId(generateOrderNo());
        orderDO orderDO =convertFromOrderModel(orderModel);

        orderDOMapper.insertSelective(orderDO);

        //加上商品的销量
        itemService.increaseSales(itemId,amount);
        //4.返回前端
        return orderModel;
    }

    /**
     *  生成订单号
     *  全局唯一性的策略防止回滚时跟随回滚
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6位位自增序列
        sequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDO.getCurrentValue();//获取未使用的sequence
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);//更新sequence以供下次使用

        String sequenceStr = String.valueOf(sequence);
        for(int i=0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
//        stringBuilder.append("000000");
        //最后2位为分库分表位00～99 用户的水平拆分 减少库表的压力 用余数通过userId 将一个用户的信息放在一张表里 暂时写死
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
    private orderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        orderDO orderDO = new orderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
