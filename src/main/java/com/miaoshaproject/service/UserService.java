package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {
    //model层才是处理业务的核心模型 data object仅仅是对数据库的映射
    UserModel getUserById(Integer id); //这里的返回值是UserModel。
    //注册流程
    void register(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone 用户注册手机
     * @param encrptPassword 用户加密后的密码
     * @throws BusinessException
     */
    UserModel validationLogin(String telphone, String encrptPassword) throws BusinessException;
}
