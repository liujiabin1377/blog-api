package com.liujiabin.service;

import com.liujiabin.dao.pojo.Tag;
import com.liujiabin.vo.TagVo;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagService {

	/*根据文章id查询标签*/
	List<TagVo> findTagsByArticleId(long id);

	/*查询最热标签*/
	List<Tag> hotTags(int limit);

	/*查询所有的标签*/
	Result allTags();

	/*查询所有细节*/
	Result findAllDetail();

	Result findDetailById(Long id);
}
