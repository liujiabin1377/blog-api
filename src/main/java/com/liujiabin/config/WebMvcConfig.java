package com.liujiabin.config;

import com.liujiabin.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration    //Mvc配置类实现WebMvcConfigurer接口
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private LoginInterceptor loginInterceptor;

	@Override    //实现跨域映射
	public void addCorsMappings(CorsRegistry registry) {
		/*注册，添加映射路径(请求后端)，允许访问源(请求前端)*/
		registry.addMapping("/**").allowedOrigins("http://127.0.0.1:8080");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)  //登入验证拦截器
			.addPathPatterns("/test")
			.addPathPatterns("/comments/create/change")    //评论请求
			.addPathPatterns("/articles/publish");      //发布文章请求
	}
}
