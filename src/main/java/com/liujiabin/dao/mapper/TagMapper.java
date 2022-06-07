package com.liujiabin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liujiabin.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

	/*根据文章id查询标签列表*/
	List<Tag> selectTagsByArticleId(long articleId);

	/*查询最热标签的ids*/
	List<Long> selectHotTagsIds(int limit);
}
