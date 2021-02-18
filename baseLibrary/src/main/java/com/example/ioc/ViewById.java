package com.example.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hjcai on 2021/2/5.
 * 注解声明
 */

@Target(ElementType.FIELD)//该注解表示只能用于类的属性
//其他常见属性如 TYPE用在类上  CONSTRUCTOR用在构造函数上 METHOD用于方法
@Retention(RetentionPolicy.RUNTIME)
public//该注解表示在运行时生效
//其他常见属性如 CLASS编译时   RUNTIME运行时  SOURCE源码级别

@interface ViewById {
    //value代表可以该注解可以添加一个参数
    int value();
}
