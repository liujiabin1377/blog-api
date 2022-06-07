package com.liujiabin.service;

import com.liujiabin.dao.dos.Archives;
import com.liujiabin.dao.pojo.Article;
import com.liujiabin.vo.ArticleVo;
import com.liujiabin.vo.params.ArticleParam;
import com.liujiabin.vo.params.PageParams;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleService {

	/*查询所有文章并分页显示*/
	List<ArticleVo> listArticle(PageParams pageParams);

	/*根据浏览量查询文章*/
	List<Article> findHotArticelByView(int limit);

	/*根据创建时间查询文章*/
	List<Article> findNewArticleByTime(int limit);

	/*文章归档*/
	List<Archives> listArchivesArticle();

	/*根据id查询文章详情*/
	ArticleVo findArticleById(Long id);

	/*发布文章*/
	Result publish(ArticleParam articleParam);
}
