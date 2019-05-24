package com.miaoshaproject;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.miaoshaproject"}) //Spring boot自动加载内嵌tomcat的配置
@RestController//使用注解 完成springMVC controller的功能
@MapperScan("com.miaoshaproject.dao")
public class App 
{
    @Autowired(required = false)
    private UserDOMapper userDOMapper;

    @RequestMapping("/") // springMVC 访问主界面 调用
    public String home(){
        UserDO UserDO=userDOMapper.selectByPrimaryKey(1);
    if (UserDO ==null) {
        return "用户对象不存在";
    }else{
        return UserDO.getName();
    }
    }
    public static void main( String[] args )
    {
        System.out.println( "application is running!" );
        SpringApplication.run(App.class,args);
    }
}
