package com.liujiabin.utils;

import com.liujiabin.dao.pojo.SysUser;


public class UserThreadLocal {

    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();   //创建一个本地线程

    public static void set(SysUser sysUser){
        //将数据设置到线程中
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        //取出线程中的数据
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}