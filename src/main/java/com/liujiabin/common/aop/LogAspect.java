package com.liujiabin.common.aop;

import com.alibaba.fastjson.JSON;

import com.liujiabin.utils.HttpContextUtils;
import com.liujiabin.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 注解式的统一日志
 * 1.开发注解
 * 2.开发环绕通知
 * 3.获取切点的执行时长
 * 4.利用反射机制，打印出切点的各种Method属性（目的）
 * 5.返回切点的执行结果
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.liujiabin.common.aop.LogAnnotation)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        long beginTime = System.currentTimeMillis();

        Object result = point.proceed();  //执行方法

        long time = System.currentTimeMillis() - beginTime;  //执行时长(毫秒)

        recordLog(point, time);  //打印日志（切点的执行时长）
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //获取切点的签名
        Method method = signature.getMethod();  //获取签名的Method
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class); //获取Method的注解
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.name());
        log.info("operation:{}",logAnnotation.operation());

        String className = joinPoint.getTarget().getClass().getName();  //切点的目标的类名
        String methodName = signature.getName();  //签名的name就是Method的name
        log.info("request method:{}",className + "." + methodName + "()");

        Object[] args = joinPoint.getArgs();  //切入点参数
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IPUtils.getIpAddr(request));  //获取request 设置IP地址

        log.info("excute time : {} ms",time);  //切点的执行时长

        log.info("=====================log end================================");
    }

}