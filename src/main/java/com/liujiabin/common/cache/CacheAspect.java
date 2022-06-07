package com.liujiabin.common.cache;

import com.alibaba.fastjson.JSON;
import com.liujiabin.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 注解式的统一缓存
 * 1.开发注解用来做切入点，注解里申明缓存时间
 * 2.开发环绕通知(需要捕获异常)
 * 2.1.利用aop反射机制获取方法的各种属性
 * 3.制定reidskey的格式
 * 4.查询一次redis，是否有value
 * 5.有value直接返回
 * 6.没有value就执行切入点方法
 * 7.将结果存入redis并返回
 */

@Aspect
@Component
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.liujiabin.common.cache.Cache)")
    public void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        try {
            Signature signature = pjp.getSignature();  //获取签名
            String className = pjp.getTarget().getClass().getSimpleName();     //类名
            String methodName = signature.getName();     //调用的方法名

            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            //参数
            String params = "";
            for(int i=0; i<args.length; i++) {
                if(args[i] != null) {
                    params += JSON.toJSONString(args[i]);  //把所有参数转成字符串拼接起来
                    parameterTypes[i] = args[i].getClass();  //把所有的参数类型保存到数据中
                }else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                params = DigestUtils.md5Hex(params);  //加密 以防出现key过长以及字符转义获取不到的情况
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);  //利用方法名和参数类型数组得到Method

            Cache annotation = method.getAnnotation(Cache.class); //获取Method上的Cache注解

            long expire = annotation.expire();  //用Cache中的方法申明 缓存过期时间

            String name = annotation.name(); //用Cache中的方法申明 缓存名称

            String redisKey = name + "::" + className+"::"+methodName+"::"+params;  //申明key：缓存名::类名::方法名::加密后参数拼接
            String redisValue = redisTemplate.opsForValue().get(redisKey);

            if (StringUtils.isNotEmpty(redisValue)){
                log.info("走了缓存~~~,{},{}",name,methodName);
                return JSON.parseObject(redisValue, Result.class);  // ！！！第一次查询redisKey如果有redisValue，直接返回结果
            }

            Object proceed = pjp.proceed();  // ！！！如果没有查询到结果，就执行切入点方法

            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire)); //！！！将切入点方法的返回值存入redis
            log.info("存入缓存~~~ {},{}",name,methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }
}