package com.liujiabin.service.impl;

import com.liujiabin.dao.mapper.ArticleBodyMapper;
import com.liujiabin.dao.pojo.ArticleBody;
import com.liujiabin.service.ArticleBodyService;
import com.liujiabin.vo.ArticleBodyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {
	@Autowired
	private ArticleBodyMapper articleBodyMapper;
	@Override
	public ArticleBodyVo findArticleBodyVoById(Long bodyId) {
		ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
		ArticleBodyVo articleBodyVo = new ArticleBodyVo();
		BeanUtils.copyProperties(articleBody, articleBodyVo);
		return articleBodyVo;
	}
}
