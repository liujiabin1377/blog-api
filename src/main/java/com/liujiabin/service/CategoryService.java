package com.liujiabin.service;

import com.liujiabin.vo.CategoryVo;
import com.liujiabin.vo.result.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryService {
	/*根据id查询文章的分类*/
	CategoryVo findCategorysById(Long categoryId);

	/*查询所有的类别信息*/
	Result allCategorys();

	/*查询所有细节*/
	Result findAllDetail();


	Result categoriesDetailById(Long id);
}
