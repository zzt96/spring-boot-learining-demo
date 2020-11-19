package com.miaoshaproject.error;

/**
 * 设计模式：包装器业务异常类实现
 * BusinessException和EMBusinessError都继承了CommonError
 * 外界不论是定义了EMBusinessError还是BusinessException 都可以实现组装定义
 * 并且需要自行定义setErrMsg的方法 把EM中的方法覆盖掉
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;
    /**
     * BusinessException构造函数
     * 直接接收EmBusinessErr的传参
     * 用于构造业务异常
     * @param commonError
     */
    public BusinessException(CommonError commonError){
        super();//exceptiond的初始化机制
        this.commonError = commonError;
    }

    /**
     * 接收自定义的errMsg的方式
     * 构造业务异常
     */
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);//这里要用this

    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
