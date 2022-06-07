package com.liujiabin.service;

import com.liujiabin.vo.ArticleBodyVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleBodyService {

	ArticleBodyVo findArticleBodyVoById(Long bodyId);
}
