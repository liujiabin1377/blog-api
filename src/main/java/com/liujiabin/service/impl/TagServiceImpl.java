package com.liujiabin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liujiabin.dao.mapper.TagMapper;
import com.liujiabin.dao.pojo.Tag;
import com.liujiabin.service.TagService;
import com.liujiabin.vo.TagVo;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {
	@Autowired
	private TagMapper tagMapper;


	@Override
	public List<TagVo> findTagsByArticleId(long articleId) {
		/*根据文章id（ms_article_tag）查询标签（ms_tag）*/
		List<Tag> tagList = tagMapper.selectTagsByArticleId(articleId);
		/*抽取Tag对象*/
		List<TagVo> tagVoList = copyList(tagList);
		return tagVoList;
	}
	public List<TagVo> copyList(List<Tag> tagList){
		ArrayList<TagVo> tagVos = new ArrayList<>();
		for (Tag tag : tagList){
			tagVos.add(copy(tag));
		}
		return tagVos;
	}
	public TagVo copy(Tag tag){
		TagVo tagVo = new TagVo();
		BeanUtils.copyProperties(tag, tagVo);
		return tagVo;
	}

	@Override
	public List<Tag> hotTags(int limit) {
		/*分组排序计数查询最热标签id（ms_article_tag）*/
		List<Long> ids = tagMapper.selectHotTagsIds(limit);
		if(CollectionUtils.isEmpty(ids)){
			return Collections.emptyList();
		}
		/*查询id对应的标签（ms_Tag）*/
		List<Tag> tagList = tagMapper.selectBatchIds(ids);
		return tagList;
	}

	@Override
	public Result allTags() {
		LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Tag::getId,Tag::getTagName);
		List<Tag> tags = this.tagMapper.selectList(queryWrapper);
		return Result.success(copyList(tags));
	}

	@Override
	public Result findAllDetail() {
		LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		List<Tag> tags = this.tagMapper.selectList(queryWrapper);
		return Result.success(copyList(tags));
	}

	@Override
	public Result findDetailById(Long id) {
		Tag tag = tagMapper.selectById(id);
		return  Result.success(copy(tag));
	}
}
