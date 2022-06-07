package com.liujiabin.controller;

import com.liujiabin.service.LoginService;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private LoginService loginService;

	@GetMapping("currentUser")
	public Result currentUser(@RequestHeader("Authorization") String token ){
		/*前端页面需要token所对应的用户信息*/
		return loginService.currentUser(token);
	}
}
