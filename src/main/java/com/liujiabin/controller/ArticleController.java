package com.liujiabin.controller;

import com.liujiabin.common.aop.LogAnnotation;
import com.liujiabin.common.cache.Cache;
import com.liujiabin.service.ArticleService;
import com.liujiabin.vo.ArticleVo;
import com.liujiabin.vo.params.ArticleParam;
import com.liujiabin.vo.result.Result;
import com.liujiabin.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*用json数据应答*/
@RestController
@RequestMapping("articles")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@PostMapping   //参数在请求体中，可以先封装参数
	@LogAnnotation(name = "articles",operation = "打印articles请求的细节")
	@Cache(expire = 5 * 60 * 1000,name = "articles")
	public Result listArticle(@RequestBody PageParams pageParams){
		/*查询所有文章并分页显示*/
		return Result.success( articleService.listArticle(pageParams) );
	}

	@PostMapping("hot")
	@Cache(expire = 5 * 60 * 1000,name = "hot_article")
	public Result hotArticle(){
		//取4条最热文章，按阅读数排序
		int limit = 4 ;
		return Result.success(articleService.findHotArticelByView(limit));
	}

	@PostMapping("new")
	@Cache(expire = 5 * 60 * 1000,name = "new_article")
	public Result newAtricle(){
		//取4条最新文章，按时间排序
		int limit = 4;
		return Result.success(articleService.findNewArticleByTime(limit));
	}

	@PostMapping("listArchives")
	public Result listArchives(){
		//归档，按年月分组并排序
		return Result.success(articleService.listArchivesArticle());
	}

	@PostMapping("view/{id}")  //参数在请求路径中
	public Result viewAriticleById(@PathVariable("id") Long id){
		//根据id查询文章，并增加浏览次数(线程池操作)
		ArticleVo articleVo = articleService.findArticleById(id);
		return Result.success(articleVo);
	}

	@PostMapping("publish")   //参数在请求体中，可以先封装参数
	public Result publish(@RequestBody ArticleParam articleParam){
		//发布文章，参数只有文章的信息，用户的信息需要在本地线程中获取
		return articleService.publish(articleParam);

	}
}
