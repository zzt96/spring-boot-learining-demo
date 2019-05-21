package com.miaoshaproject.response;

public class CommonReturnType {
    private String status;//表明对应请求的返回处理结果 success或fail


    //若status是success 则data内返回json数据
    //若为fail 则data内返回通用的错误码格式
    private Object data;

    //创建一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
