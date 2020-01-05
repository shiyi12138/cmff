package com.baizhi.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RedisAop {
    //注入redisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Around("@annotation(com.baizhi.annotation.MyInclude)")
    //该方法添加缓存又获取缓存
    public Object around(ProceedingJoinPoint point){
        HashOperations hash = redisTemplate.opsForHash();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //获取目标方法所在类的全限定名作为dakey
        String nameSpace = point.getTarget().getClass().getName();
        //获取目标方法和参数拼接作为小key
        String name = point.getSignature().getName();
        Object[] args = point.getArgs();
        //使用StringBuild拼接
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(name);
        for (Object arg : args) {
            //转化为String拼接
            stringBuilder.append(arg.toString()+"_");
        }
        if (hash.hasKey(nameSpace,stringBuilder)){
            Object o = hash.get(nameSpace, stringBuilder);
            System.out.println("获取缓存");
            return o;
        }
        //没有就查询数据库
        try {
            Object proceed = point.proceed();
            hash.put(nameSpace,stringBuilder,proceed);
            System.out.println("添加缓存");
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    //清除缓存
    @AfterReturning("@annotation(com.baizhi.annotation.ClearCache)")
    public void clear(JoinPoint joinPoint){
        String nameSpace = joinPoint.getTarget().getClass().getName();
        stringRedisTemplate.delete(nameSpace);
        System.out.println("清除缓存");
    }
}
