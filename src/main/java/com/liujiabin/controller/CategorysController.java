package com.liujiabin.controller;

import com.liujiabin.service.CategoryService;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategorysController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public Result allCategorys(){
		//查询所有类别的id和name
		return categoryService.allCategorys();
	}

	@GetMapping("detail")
	public Result categoriesDetail(){
		//查询所有的类别集合
		return categoryService.findAllDetail();
	}

	@GetMapping("detail/{id}")
	public Result categoriesDetailById(@PathVariable("id") Long id){
		//根据id查询类别
		return categoryService.categoriesDetailById(id);
	}
}
