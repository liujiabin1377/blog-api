package com.liujiabin.controller;

import com.liujiabin.service.LoginService;
import com.liujiabin.vo.params.LoginParams;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@PostMapping
	public Result login(@RequestBody LoginParams loginParams){
		/*登入返回给用户token*/
		return  loginService.login(loginParams);
	}
}
