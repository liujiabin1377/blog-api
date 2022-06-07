package com.liujiabin.interceptor;

import com.alibaba.fastjson.JSON;


import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.LoginService;
import com.liujiabin.utils.UserThreadLocal;
import com.liujiabin.vo.ErrorCode;
import com.liujiabin.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1.登入验证拦截器 implements HandlerInterceptor
 * 2.重写执行前方法 preHandle
 *      判断是否是需要拦截的请求
 *      获取请求头中token
 *      判断token是否为空
 *      判断token是否可以返回实例
 *      将实例设置到本地线程（因为用户实例的信息是线程共享信息）
 * 3.重写执行后方法 afterCompletion
 *      移除本地线程
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ( !(handler instanceof HandlerMethod) ){
            return true;    //如果不是mvc需要拦截的请求直接放行
        }
        String token = request.getHeader("Authorization");  //请求域中取出Authorization是token在请求头中的name
        /*lombok的日志打印*/
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (token == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8"); //设置响应格式
            response.getWriter().print(JSON.toJSONString(result));  //响应用输出流打印结构
            return false;
        }

        SysUser sysUser = loginService.checkToken(token);   //调用loginService的token检查方法

        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        UserThreadLocal.set(sysUser);   //是登录状态放行，把用户信息存放到本地线程
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
