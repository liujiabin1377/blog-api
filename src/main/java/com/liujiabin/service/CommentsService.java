package com.liujiabin.service;

import com.liujiabin.vo.params.CommentParams;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsService {

	Result commentsByArticleId(Long articleId);

	/*创建或者更新评论*/
	Result changeComment(CommentParams commentParams);
}
