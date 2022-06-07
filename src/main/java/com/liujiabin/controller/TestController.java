package com.liujiabin.controller;

import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.utils.UserThreadLocal;
import com.liujiabin.vo.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}