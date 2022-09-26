package com.dengwei.job;

import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.entity.Article;
import com.dengwei.service.ArticleService;
import com.dengwei.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Denwher
 * @version 1.0
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY_PREFIX);
        //对viewCountMap进行转化
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //将浏览量更新到数据库中
        //报错的代码：articleService.updateBatchById(articles);
        articleService.updateBatchViewCount(articles);
    }
}
