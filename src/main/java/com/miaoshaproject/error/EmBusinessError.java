package com.miaoshaproject.error;

public enum EmBusinessError implements CommonError {
    //枚举类型在这里填值
    //通用错误类型 1000
    PARAMTER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),

    //20000开头为用户相关错误信息定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确")
    ;

    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;//java 枚举类型
        this.errMsg = errMsg;
    }
    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this; //使用这种方式 改动通用错误码的errMsg

    }
}
