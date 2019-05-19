package com.miaoshaproject.service;

import com.miaoshaproject.service.model.UserModel;

public interface UserService {
    //model层才是处理业务的核心模型 data object仅仅是对数据库的映射
    UserModel getUserById(Integer id); //这里的返回值是UserModel。
}
