package com.liujiabin.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Article {
	/*点赞数*/
	public static final int Article_TOP = 1;
	/*评论数*/
	public static final int Article_Common = 0;

	//主键id，默认雪花算法
	private Long id;
	/*标题*/
	private String title;
	/*概要*/
	private String summary;
	/*评论数量*/
	private Integer commentCounts;
	/*浏览数量*/
	private Integer viewCounts;
	/*作者id*/
	private Long authorId;
	/*内容id*/
	private Long bodyId;
	/*类别id*/
	private Long categoryId;
	/*权重置顶*/
	private Integer weight ;
	/*创建时间*/
	private Long createDate;
}

