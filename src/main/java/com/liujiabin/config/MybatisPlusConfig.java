package com.liujiabin.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.liujiabin.dao.mapper") //包扫描
public class MybatisPlusConfig {

	@Bean   //配置MybatisPlus拦截器
	public MybatisPlusInterceptor mybatisPlusInterceptor(){
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();      //创建MP全局拦截器
		interceptor.addInnerInterceptor ( new PaginationInnerInterceptor() );   //添加分页拦截器
		return interceptor;
	}
}
