package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 用户登陆接口
     */
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone") String telphone,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(password)||StringUtils.isEmpty(telphone)){
            throw new BusinessException(EmBusinessError.PARAMTER_VALIDATION_ERROR);

        }
        //用户登陆服务 校验用户登陆是否合法
        UserModel userModel = userService.validationLogin(telphone,this.EncodeByMd5(password));

        //加入到用户登陆成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);//islogin 的会话标示 表示用户登陆成功
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户注册接口
     * @param telphone
     * @param otpCode
     * @param name
     * @param gender
     * @param age
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone") String telphone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") byte gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="password") String password
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号与对应的otpcode是否符合
        String insessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,insessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMTER_VALIDATION_ERROR,"短信验证码不符合");

        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);


        return CommonReturnType.create(null);
    }

    public String  EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;

    }
    /**
     * 用户获取otp短信接口
     */

    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
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

    /**
     * 通过id获取用户对象
     * @param id
     * @return
     * @throws BusinessException
     */
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
