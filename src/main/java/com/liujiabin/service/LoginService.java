package com.liujiabin.service;

import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.vo.params.LoginParams;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
@Transactional
public interface LoginService {

	Result login(LoginParams loginParams);

	Result delectToken(String token);

	Result register(LoginParams loginParams);

	SysUser checkToken(String token);

	Result currentUser(String token);
}
