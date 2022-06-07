package com.liujiabin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liujiabin.dao.mapper.CategoryMapper;
import com.liujiabin.dao.pojo.Article;
import com.liujiabin.dao.pojo.Category;
import com.liujiabin.service.CategoryService;
import com.liujiabin.vo.ArticleVo;
import com.liujiabin.vo.CategoryVo;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public CategoryVo findCategorysById(Long categoryId) {
		Category category = categoryMapper.selectById(categoryId);
		CategoryVo categoryVo = copy(category);
		return categoryVo;
	}

	@Override
	public Result allCategorys() {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Category::getId,Category::getCategoryName);
		List<Category> categories = categoryMapper.selectList(queryWrapper);
		return Result.success(copyList(categories));
	}

	@Override
	public Result findAllDetail() {
		List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
		return Result.success(copyList(categories));
	}

	@Override
	public Result categoriesDetailById(Long id) {
		Category category = categoryMapper.selectById(id);
		CategoryVo categoryVo = copy(category);
		return Result.success(categoryVo);
	}

	public CategoryVo copy(Category category){
		CategoryVo categoryVo = new CategoryVo();
		BeanUtils.copyProperties(category,categoryVo);
		return categoryVo;
	}
	public List<CategoryVo> copyList(List<Category> categories){
		List<CategoryVo> categoryVoList = new ArrayList<>();
		for (Category category : categories) {
			categoryVoList.add(copy(category));
		}
		return categoryVoList;
	}

	public List<CategoryVo> copyList(Category category){
		ArrayList<CategoryVo> categoryVoList = new ArrayList<>();
		/*循环集合添加copy后的vo对象*/
		for ( CategoryVo categoryVo :  categoryVoList){
			CategoryVo vo = new CategoryVo();
			BeanUtils.copyProperties(category,vo);
			categoryVoList.add(vo);
		}
		return categoryVoList;
	}
}
