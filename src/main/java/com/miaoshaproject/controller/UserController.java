package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户获取otp短信接口
    @RequestMapping("/getotp")
    @ResponseBody
    public CommonReturnType getotp(@RequestParam(name="telphone") String telphone){
        //按一定规则生成otp验证码
        Random random = new Random();
        int randomint = random.nextInt(99999);
        randomint +=10000;
        String otpCode = String.valueOf(randomint);

        //将otp验证码与对应用户的手机号相关联 使用http session的方式 绑定
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        //将otpCode 发送给用户
        System.out.println("telphone = " + telphone + " & otpCode = " + otpCode);


        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
    //调用service服务获取id用户对象并返回给前段
      UserModel userModel= userService.getUserById(id);

      //若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);//抛出异常到tomcat的容器层
        }
      //将核心领域模型用户对象 转换为可供前端使用的view object
      UserVO userVO = convertFromModel(userModel);

      //返回通用对象
      return CommonReturnType.create(userVO);

    }
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);//从userModel copy到userVO
        return userVO;
    }


}
