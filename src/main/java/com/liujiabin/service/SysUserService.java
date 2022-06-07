package com.liujiabin.service;

import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.vo.UserVo;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserService {

	/*根据文章中的作者id查询用户中的昵称*/
	String findUserById(long authorId);

	/*查询账户的信息*/
	SysUser findUser(String account, String password);

	SysUser findUserByAccoun(String account);

	int save(SysUser newUser);

	UserVo findUserVoById(Long articleId);
}
