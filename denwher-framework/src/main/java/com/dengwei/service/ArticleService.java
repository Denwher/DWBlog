package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.ArticleDto;
import com.dengwei.domain.entity.Article;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
public interface ArticleService extends IService<Article> {
    ResponseResult getHotArticleList();

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult getArticlePageList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult updateArticle(ArticleDto articleDto);

    ResponseResult deleteArticle(String id);

    void updateBatchViewCount(List<Article> articles);
}
