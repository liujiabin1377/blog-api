package com.liujiabin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.LoginService;
import com.liujiabin.service.SysUserService;
import com.liujiabin.utils.JWTUtils;
import com.liujiabin.vo.ErrorCode;
import com.liujiabin.vo.LoginUserVo;
import com.liujiabin.vo.params.LoginParams;
import com.liujiabin.vo.result.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private RedisTemplate redisTemplate;

	private static final String slat = "mszlu!@#";

	@Override
	public Result login(LoginParams loginParams) {
		/**
		 * 1.验证账户密码是否存在
		 * 2.将密码加盐
		 * 3.生成token存入redis
		 * 4.返回token
		 */
		String account = loginParams.getAccount();
		String password = loginParams.getPassword();

		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
			return Result.fail(ErrorCode.ACCOUNT_BLANK.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
		}

		String pwd = DigestUtils.md5Hex(password + slat);   //将密码加盐
		SysUser sysUser = sysUserService.findUser(account,pwd);
		if (sysUser == null){
			return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
		}

		String token = JWTUtils.createToken(sysUser.getId());   //取出id生成token
		redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);  //把token存入key,实例转换成json存入value
		return Result.success(token);
	}

	@Override
	public Result register(LoginParams loginParams) {
		/**
		 * 1.判断参数是否为空
		 * 2.查询账户是否存在
		 * 3.不存在就创建一个新用户，并保存
		 * 4.生成token，存入redis
		 * 5.返回token
		 */
		String account = loginParams.getAccount();
		String password = loginParams.getPassword();
		String nickname = loginParams.getNickname();

		if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
			return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
		}
		SysUser sysUser = sysUserService.findUserByAccoun(account);
		if (sysUser != null){
			return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
		}
		SysUser newUser = new SysUser();
		newUser.setNickname(nickname);
		newUser.setAccount(account);
		newUser.setPassword(DigestUtils.md5Hex(password+slat));
		newUser.setCreateDate(System.currentTimeMillis());
		newUser.setLastLogin(System.currentTimeMillis());
		newUser.setAvatar("/static/img/logo.b3a48c0.png");
		newUser.setAdmin(1); //1 为true
		newUser.setDeleted(0); // 0 为false
		newUser.setSalt("");
		newUser.setStatus("");
		newUser.setEmail("");
		int i = sysUserService.save(newUser);
		String token = JWTUtils.createToken(newUser.getId());
		redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
		return Result.success(token);
	}

	@Override
	public SysUser checkToken(String token) {
		/**
		 * 1.判断token是否为空
		 * 2.验证token是否造假
		 * 3.取出redis中的数据
		 * 4.返回实例
		 */
		if (StringUtils.isBlank(token)){
			return null;
		}
		Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
		if (stringObjectMap == null){
			return null;
		}
		String userJson = (String) redisTemplate.opsForValue().get("TOKEN_" + token); //取出来的是Json，可以强转成String
		if (StringUtils.isBlank(userJson)){
			return null;
		}
		SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
		return sysUser;
	}


	@Override
	public Result currentUser(String token) {
		if (StringUtils.isBlank(token)){
			return null;
		}
		String userjson = (String) redisTemplate.opsForValue().get("TOKEN_" + token);
		if (StringUtils.isBlank(userjson)){
			return null;
		}
		SysUser sysUser = JSON.parseObject(userjson, SysUser.class);

		LoginUserVo loginUserVo = new LoginUserVo();
		loginUserVo.setId(sysUser.getId());
		loginUserVo.setAccount(sysUser.getAccount());
		loginUserVo.setAvatar(sysUser.getAvatar());
		loginUserVo.setNickname(sysUser.getNickname());
		return Result.success(loginUserVo);
	}

	@Override
	public Result delectToken(String token) {
		redisTemplate.delete("TOKEN_"+token);
		return Result.success(null);
	}

}
