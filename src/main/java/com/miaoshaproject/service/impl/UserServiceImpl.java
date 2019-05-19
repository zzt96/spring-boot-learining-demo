package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired(required = false)
    private UserDOMapper userDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        // 调用userdomapper 获取到对应的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if(userDO == null){
            return null;
        }

        //通过用户id获取对应的密码
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        //获得所需的可操作usermodel
        return convertDataObject(userDO,userPasswordDO);

    }

    // 将DataObject里的数据转换到model层进行操作
    private UserModel convertDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel); // 复制属性

        if(userPasswordDO != null) {//保护没有密码的情况
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());//有一个ID是重复的 所以用get set方法复制密码
        }
        return userModel;
    }
}
