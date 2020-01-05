package com.baizhi.advice;

import com.alibaba.fastjson.JSON;
import com.baizhi.entity.Echarts;
import com.baizhi.service.UserService;
import io.goeasy.GoEasy;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class MyAdvice {
    @Autowired
    private UserService userService;
    @AfterReturning(pointcut="execution(* com.baizhi.service.UserService.del(..))")
    public void after(){
        int[] ints = userService.selectMonthNum();
        int[] ints1 = userService.select7Num();
        String s = JSON.toJSONString(ints);
        String s1 = JSON.toJSONString(ints1);
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io","BC-002a017172624d18834aaa69679edeb5");
        //分别推送，分别取
        goEasy.publish("month", s);
        goEasy.publish("seven", s1);
    }
}
