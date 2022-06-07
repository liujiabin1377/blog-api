package com.liujiabin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liujiabin.dao.mapper.CommentsMapper;
import com.liujiabin.dao.pojo.Comment;
import com.liujiabin.dao.pojo.SysUser;
import com.liujiabin.service.CommentsService;
import com.liujiabin.service.SysUserService;
import com.liujiabin.utils.UserThreadLocal;
import com.liujiabin.vo.CommentVo;
import com.liujiabin.vo.UserVo;
import com.liujiabin.vo.params.CommentParams;
import com.liujiabin.vo.result.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
	@Autowired
	private CommentsMapper commentsMapper;
	@Autowired
	private SysUserService sysUserService;

	@Override
	public Result commentsByArticleId(Long articleId) {
		LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Comment::getArticleId, articleId);
		List<Comment> commentList = commentsMapper.selectList(queryWrapper);
		List<CommentVo> CommentVoList = copyList(commentList);
		return Result.success(CommentVoList);
	}

	@Override
	public Result changeComment(CommentParams commentParams) {
		SysUser sysUser = UserThreadLocal.get();
		Comment comment = new Comment();
		comment.setAuthorId(sysUser.getId());  //登入用户的id
		comment.setArticleId(commentParams.getArticleId()); //文章的id
		comment.setContent(commentParams.getContent());
		comment.setCreateDate(System.currentTimeMillis());
		if (commentParams.getParent()==null || commentParams.getParent()==0){
			comment.setLevel(1);
			comment.setParentId(0l);
		}else{
			comment.setLevel(2);
			comment.setParentId(commentParams.getParent());
		}
		comment.setToUid(commentParams.getToUserId()==null ? 0L : commentParams.getToUserId());  //评论要@的id
		commentsMapper.insert(comment);
		return Result.success(comment);
	}

	public List<CommentVo> copyList(List<Comment> commentList){
		ArrayList<CommentVo> commentVos = new ArrayList<>();
		for (Comment comment : commentList){
			commentVos.add( copy(comment) );
		}
		return commentVos;
	}

	public CommentVo copy(Comment comment){
		CommentVo commentVo = new CommentVo();
		BeanUtils.copyProperties(comment, commentVo);
		Long articleId = comment.getArticleId();
		UserVo userVo = sysUserService.findUserVoById(articleId);
		commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
		commentVo.setAuthor(userVo);
		commentVo.setChildrens( findChildrensByid(articleId) );
		if (comment.getLevel() > 1){
			Long toUid = comment.getToUid();
			UserVo toUserVo = sysUserService.findUserVoById(toUid);
			commentVo.setToUser(toUserVo);
		}
		return commentVo;
	}

	/*查询所有子评论*/
	public List<CommentVo> findChildrensByid(Long articleId){
		LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Comment::getId, articleId)
			.eq(Comment::getLevel, 2);
		return copyList( commentsMapper.selectList(queryWrapper) );
	}
}
