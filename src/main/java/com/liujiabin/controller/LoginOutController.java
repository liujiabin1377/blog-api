package com.liujiabin.controller;

import com.liujiabin.service.LoginService;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LoginOutController {
	@Autowired
	private LoginService loginService;

	@GetMapping  //参数在请求头中（token在请求头中的name是Authorization）
	public Result loginOut(@RequestHeader("Authorization")String token){
		/*登出 删除redis中的key(token)*/
		return loginService.delectToken(token);
	}
}
