package com.liujiabin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liujiabin.dao.dos.Archives;
import com.liujiabin.dao.mapper.ArticleBodyMapper;
import com.liujiabin.dao.mapper.ArticleMapper;
import com.liujiabin.dao.mapper.ArticleTagMapper;
import com.liujiabin.dao.pojo.Article;
import com.liujiabin.dao.pojo.ArticleBody;
import com.liujiabin.dao.pojo.ArticleTag;
import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.*;
import com.liujiabin.utils.UserThreadLocal;
import com.liujiabin.vo.ArticleVo;
import com.liujiabin.vo.CategoryVo;
import com.liujiabin.vo.TagVo;
import com.liujiabin.vo.params.ArticleParam;
import com.liujiabin.vo.params.PageParams;
import com.liujiabin.vo.result.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private TagService tagService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ArticleBodyService articleBodyService;
	@Autowired
	private CategoryService categoryService;

	/*查询所有文章并分页显示*/
	public List<ArticleVo> listArticle(PageParams pageParams) {
		Page<Article> page = new Page<>( pageParams.getPage(), pageParams.getPageSize() );
		/*把page对象放在第一位参数就可以按分页显示（MP自动完成拦截）*/
		IPage<Article> articleIPage = articleMapper.selectArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
		List<ArticleVo> articleVos = copyList(articleIPage.getRecords(), true, true);
		return articleVos;
	}
/*	@Override
	public List<ArticleVo> listArticle(PageParams pageParams) {
		*//*创建page对象，arg1:当前页码，arg2:页码大小*//*
		Page<Article> page = new Page<>( pageParams.getPage(), pageParams.getPageSize() );
		*//*创建构造器*//*
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		*//*后加程序，判断参数中有没有CategoryId*//*
		if (pageParams.getCategoryId() != null) {
			queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
		}
		*//*后加程序，判断参数中有没有TagId*//*
		ArrayList<Long> articleIds = new ArrayList<Long>();
		if (pageParams.getTagId() != null){
			*//*关联表查询所有的文章id*//*
			LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
			articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
			List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
			for (ArticleTag articleTag : articleTags) {
				articleIds.add(articleTag.getArticleId());
			}
			queryWrapper.in(Article::getId, articleIds);
		}
		*//*时间倒序，置顶倒序*//*
		queryWrapper.orderByDesc(Article::getCreateDate, Article::getWeight);
		*//*调用MP方法分页查询文章*//*
		Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
		*//*得到页码记录*//*
		List<Article> articleList = articlePage.getRecords();
		*//*抽取Article对象*//*
		List<ArticleVo> articleVoList = copyList(articleList , true , true);
		return articleVoList;
	}*/



	/*根据浏览量查询文章*/
	@Override
	public List<Article> findHotArticelByView(int limit) {
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Article::getViewCounts)
			.select(Article::getId,Article::getTitle)
			.last("limit "+limit);
		List<Article> articles = articleMapper.selectList(queryWrapper);

		return articles;
	}

	/*根据创建时间查询文章*/
	@Override
	public List<Article> findNewArticleByTime(int limit) {
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Article::getCreateDate )
			.last("limit "+limit);
		List<Article> articles = articleMapper.selectList(queryWrapper);
		return articles;
	}

	/*根据年月归档并计数*/
	@Override
	public List<Archives> listArchivesArticle() {
		List<Archives> articles = articleMapper.listArchivesArticle();
		return articles;
	}

	@Autowired
	private ThreadService threadService;
	@Override
	public ArticleVo findArticleById(Long id) {
		Article article = articleMapper.selectById(id);
		ArticleVo articleVo = copy(article,true,true,true,true);
		/*添加一个更新阅读数的操作（使用线程池来完成这个操作）*/
		threadService.updateViewCount(articleMapper, article);
		return articleVo;
	}

	@Autowired
	private ArticleTagMapper articleTagMapper;
	@Autowired
	private ArticleBodyMapper articleBodyMapper;
	@Override
	@Transactional
	public Result publish(ArticleParam articleParam) {
		SysUser sysUser = UserThreadLocal.get(); //获取本地线程数据，先要拦截请求

		Article article = new Article();
		article.setAuthorId(sysUser.getId());  //文章id要在本地线程中的实例中获取
		article.setCategoryId(articleParam.getCategory().getId()); //类别id
		article.setCreateDate(System.currentTimeMillis());
		article.setCommentCounts(0);
		article.setSummary(articleParam.getSummary());
		article.setTitle(articleParam.getTitle());
		article.setViewCounts(0);
		article.setWeight(Article.Article_Common);
		articleMapper.insert(article);

		List<TagVo> tags = articleParam.getTags();
		if (tags != null) {
			for (TagVo tag : tags) {
				ArticleTag articleTag = new ArticleTag();
				articleTag.setArticleId(article.getId());
				articleTag.setTagId(tag.getId());
				articleTagMapper.insert(articleTag);
			}
		}
		ArticleBody articleBody = new ArticleBody();
		articleBody.setContent(articleParam.getBody().getContent());
		articleBody.setContentHtml(articleParam.getBody().getContentHtml());
		articleBody.setArticleId(article.getId());
		articleBodyMapper.insert(articleBody);

		article.setBodyId(articleBody.getId());   //BodyId需要再插入后才会自动生成
		articleMapper.updateById(article);   //全部插入完成后更新对象

		Map<String ,String> map = new HashMap<>();
		map.put("id", article.getId().toString());
		return Result.success(map);
	}

	/*copy实体类集合*/
	private List<ArticleVo> copyList(List<Article> articleList , Boolean isTag , Boolean isAuthor) {
		ArrayList<ArticleVo> articleVoList = new ArrayList<>();
		/*循环集合添加copy后的vo对象*/
		for ( Article article : articleList ){
			articleVoList.add( copy( article , isTag , isAuthor));
		}
		return articleVoList;
	}
	/*copy实体类*/
	private ArticleVo copy(Article article , Boolean isTag , Boolean isAuthor ){
		ArticleVo articleVo = new ArticleVo();
		BeanUtils.copyProperties(article, articleVo);   //spring工具类copy属性
		articleVo.setCreateDate( new DateTime( article.getCreateDate() ).toString("yyyy-MM-dd HH:mm") );   //源类中的“时间类型”属性，设置给vo对象
		if (isTag){
			Long articleId = article.getId();
			articleVo.setTags(tagService.findTagsByArticleId(articleId));
		}
		if (isAuthor){
			Long authorId = article.getAuthorId();
			articleVo.setAuthor(sysUserService.findUserById(authorId));
		}
		return  articleVo;
	}

	/*重载方法：copy实体类*/
	private ArticleVo copy(Article article , Boolean isTag , Boolean isAuthor , Boolean isBody , Boolean isCategory){
		ArticleVo articleVo = new ArticleVo();
		BeanUtils.copyProperties(article, articleVo);  //spring工具类copy属性
		articleVo.setCreateDate( new DateTime( article.getCreateDate() ).toString("yyyy-MM-dd HH:mm") );  //源类中的“时间类型”属性
		if (isTag){
			Long articleId = article.getId();
			articleVo.setTags(tagService.findTagsByArticleId(articleId));
		}
		if (isAuthor){
			Long authorId = article.getAuthorId();
			articleVo.setAuthor(sysUserService.findUserById(authorId));
		}
		if(isBody){
			Long bodyId = article.getBodyId();
			articleVo.setBody(articleBodyService.findArticleBodyVoById(bodyId));
		}
		if (isCategory){
			Long categoryId = article.getCategoryId();
			CategoryVo categoryVo = categoryService.findCategorysById(categoryId);
			ArrayList<CategoryVo> categoryVos = new ArrayList<>();
			categoryVos.add(categoryVo);
			articleVo.setCategorys(categoryVos);
		}
		return  articleVo;
	}

}
