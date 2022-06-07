package com.liujiabin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liujiabin.dao.mapper.SysUserMapper;
import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.SysUserService;
import com.liujiabin.vo.UserVo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public String findUserById(long authorId) {
		/*根据文章中的作者id查询用户中的昵称*/
		String nickname = sysUserMapper.selectById(authorId).getNickname();
		if (nickname == null){
			nickname="小白";
		}
		return nickname;
	}

	@Override
	public SysUser findUser(String account, String password) {
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysUser::getAccount, account)
			.eq(SysUser::getPassword, password)
			.select(SysUser::getAccount,SysUser::getNickname,SysUser::getId,SysUser::getAvatar)
			.last("limit 1");
		SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
		return sysUser;
	}


	@Override
	public SysUser findUserByAccoun(String account) {
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysUser::getAccount, account)
			.last("limit 1");
		return sysUserMapper.selectOne(queryWrapper);
	}

	@Override
	public int save(SysUser newUser) {
		//注意 默认生成的id 是分布式id 采用了雪花算法
		return sysUserMapper.insert(newUser);
	}

	@Override
	public UserVo findUserVoById(Long articleId) {
		SysUser sysUser = sysUserMapper.selectById(articleId);
		UserVo userVo = new UserVo();
		userVo.setAvatar(sysUser.getAvatar());
		userVo.setNickname(sysUser.getNickname());
		userVo.setId(sysUser.getId());
		return userVo;
	}

}
