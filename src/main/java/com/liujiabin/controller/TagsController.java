package com.liujiabin.controller;

import com.liujiabin.service.TagService;
import com.liujiabin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {
	@Autowired
	private TagService tagService;

	@RequestMapping("hot")
	public Result HotTags(){
		/*查询最热标签*/
		int limit = 6 ;
		return Result.success(tagService.hotTags(limit));
	}

	@GetMapping
	public Result allTags(){
		//查询所有tag的id，name
		return tagService.allTags();
	}

	@GetMapping("detail")
	public Result findAllDetail(){
		//查询所有tag集合
		return tagService.findAllDetail();
	}

	@GetMapping("detail/{id}")
	public Result findDetailById(@PathVariable("id") Long id){
		//根据id查询tag
		return tagService.findDetailById(id);
	}
}
