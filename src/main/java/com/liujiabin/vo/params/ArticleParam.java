package com.liujiabin.vo.params;

import com.liujiabin.vo.CategoryVo;
import com.liujiabin.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}