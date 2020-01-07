package com.baizhi.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MyUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    //获取ioc容器
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    //通过类对象获取类的对象
    public static Object getBean(Class clazz){
        Object bean = applicationContext.getBean(clazz);
        return bean;
    }
}
