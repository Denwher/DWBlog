package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.ArticleDto;
import com.dengwei.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") String id){
        return articleService.deleteArticle(id);
    }

    @GetMapping("/list")
    public ResponseResult articlePageList(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.getArticlePageList(pageNum,pageSize,title,summary);
    }


}
