package com.liujiabin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liujiabin.dao.mapper.ArticleMapper;
import com.liujiabin.dao.pojo.Article;
import com.liujiabin.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor") //选择线程池异步中的任务执行者
    public void updateViewCount(ArticleMapper articleMapper, Article article){

        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);  //取出源Article的阅读数+1

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        queryWrapper.eq(Article::getViewCounts,article.getViewCounts());//(模拟乐观锁，只有值没变的情况下再更新)
        articleMapper.update(articleUpdate,queryWrapper); //参数1：要更新的对象，参数2：条件选择器
        /*
        try {
            Thread.sleep(5000);//睡眠5秒 证明不会影响主线程的使用
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
}