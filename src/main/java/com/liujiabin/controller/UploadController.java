package com.liujiabin.controller;

import com.liujiabin.utils.QiniuUtils;
import com.liujiabin.vo.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 图片上传请求
 */
@RestController
@RequestMapping("upload")
public class UploadController {
	@Autowired
	private QiniuUtils qiniuUtils;
	@PostMapping
	public Result upload(@RequestParam("image")MultipartFile multipartFile){
		/*随机数+.+原文件名的格式名*/
		String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");  //substringAfterLast 分割的后面部分
		boolean upload = qiniuUtils.upload(multipartFile, fileName);
		if (upload){
			return Result.success(QiniuUtils.url + fileName);
		}
		return Result.fail(20001,"上传失败");

	}
}
