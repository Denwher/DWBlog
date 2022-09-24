package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Article;
import com.dengwei.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        return articleService.getHotArticleList();
    }

    //查询文章列表的分页信息
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.getArticleList(pageNum,pageSize,categoryId);
    }

    //根据id查询文章详情
    @GetMapping("/{id}")
    public ResponseResult articleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


    //在redis中更新viewCount
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
