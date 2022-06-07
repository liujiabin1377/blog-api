package com.liujiabin.hander;

import com.liujiabin.vo.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice     //给所有@RestController注解的方法添加切面
public class AllExceptionHander {

	@ExceptionHandler(Exception.class)      //异常处理器，该注解会自动处理异常
	public Result doException(Exception exception){
		exception.printStackTrace();
		return Result.fail(-999, "系统出现异常");
	}
}
