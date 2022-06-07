package com.liujiabin.controller;

import com.liujiabin.service.CommentsService;
import com.liujiabin.vo.params.CommentParams;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
	@Autowired
	private CommentsService commentsService;

	@GetMapping("article/{id}")
	public Result comments(@PathVariable("id") Long articleId){
		//根据文章id查询评论
		return commentsService.commentsByArticleId(articleId);
	}

	@PostMapping("create/change")
	public Result changeComment(@RequestBody CommentParams commentParams){
		//写评论(获取本地线程中的用户信息)
		return commentsService.changeComment(commentParams);
	}
}
