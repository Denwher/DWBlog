package com.dengwei.runner;

import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.entity.Article;
import com.dengwei.domain.entity.User;
import com.dengwei.mapper.ArticleMapper;
import com.dengwei.mapper.UserMapper;
import com.dengwei.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Denwher
 * @version 1.0
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //将数据库中的viewCount数据，在启动时更新到Redis中
        //获取文章列表
        List<Article> articleList = articleMapper.selectList(null);
        //建立文章id-viewCount的关系表
        Map<String,Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> {
                    return article.getId().toString();
                }, article -> {
                    return article.getViewCount().intValue();
                }));
        //存入redis
        redisCache.setCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY_PREFIX,viewCountMap);
    }
}
