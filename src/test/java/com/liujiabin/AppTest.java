package com.liujiabin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liujiabin.dao.mapper.ArticleMapper;
import com.liujiabin.dao.mapper.SysUserMapper;
import com.liujiabin.dao.pojo.Article;
import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.*;
import com.liujiabin.utils.JWTUtils;
import com.liujiabin.vo.ArticleVo;
import com.liujiabin.vo.params.LoginParams;
import com.liujiabin.vo.params.PageParams;
import com.liujiabin.vo.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class AppTest {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private LoginService loginService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private ArticleBodyService articleBodyService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CommentsService commentsService;

	@Test
	public void listArticleTest(){
		Article article = articleMapper.selectById(1);
		System.out.println(article);
	}

	@Test
	public void hot(){
		System.out.println(articleService.findHotArticelByView(3));
	}

	@Test
	public void test(){
		LoginParams loginParams = new LoginParams("aaaa", "123456", "456");
		Result register = loginService.register(loginParams);
		System.out.println(register);
	}

	@Test
	public void test1(){
/*		List<ArticleVo> articleVos = articleService.listArticle(new PageParams(1, 10,null,7l));
		System.out.println(articleVos);*/

	}

	@Test
	public void test02(){
		ArticleVo articleVo = articleService.findArticleById(1405916999732707330l);
		System.out.println("============"+articleVo);
	}

	@Test
	public void test03(){
		System.out.println(commentsService.commentsByArticleId(1L));
	}


	@Test
	public void test04(){
		System.out.println(articleService.findArticleById(1l));
	}


}
