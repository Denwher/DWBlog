package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.ArticleDto;
import com.dengwei.domain.entity.Article;
import com.dengwei.domain.entity.ArticleTag;
import com.dengwei.domain.entity.Category;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.ArticleDetailVo;
import com.dengwei.domain.vo.ArticleListVo;
import com.dengwei.domain.vo.HotArticleVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.exception.SystemException;
import com.dengwei.mapper.ArticleMapper;
import com.dengwei.service.ArticleService;
import com.dengwei.service.ArticleTagService;
import com.dengwei.service.CategoryService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Denwher
 * @version 1.0
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    //查询热门文章, 封装成ResponseResult返回
    @Override
    public ResponseResult getHotArticleList() {
        //设置查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>();
        //正式的文章（非草稿）
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)//将字面量设置为常量
                //按照浏览量进行排序
                .orderByDesc(Article::getViewCount);
        //最多只查询前10条
        //方法一：使用limit
        //方法二：使用page类
        Page<Article> page = new Page<>(1,10);
        page(page, wrapper);
        //使用vo对返回的数据进行优化
        //热门文章需要展示的只有 id，title，viewCount
        List<Article> articleList = page.getRecords();
        //stream流方式：从redis中查询viewCount，封装到articleList
        articleList.stream()
                .map(article -> article.setViewCount(getViewCountFromRedis(article.getId()).longValue()))
                .collect(Collectors.toList());

        //bean拷贝
        //ArrayList<HotArticleVo> articleVos = new ArrayList<>();
        //for (Article article : articleList) {
        //HotArticleVo vo = new HotArticleVo();
        //BeanUtils.copyProperties(article,vo);
        //articleVos.add(vo);
        //}
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articleList, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //判断categoryId，不为空且大于0才能加入查询条件
        wrapper.eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId)
                //只能查询正式发布的文章
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
                //置顶的文章要显示在最前面
                .orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<Article> articleList = page.getRecords();

        //for循环方式：查询分类标签名，封装到articleList
//        for (Article article : articleList) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //stream流方式：查询分类标签名，封装到articleList
        articleList.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //stream流方式：从redis中查询viewCount，封装到articleList
        articleList.stream()
                .map(article -> article.setViewCount(getViewCountFromRedis(article.getId()).longValue()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticlePageList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //like默认是“%值%”的方式匹配
        //如果是“%值”和“值%”，可以用like
        //判断summary和title，不为空且“”才能加入查询条件
        wrapper.like(StringUtils.hasText(title),Article::getTitle,title)
                .like(StringUtils.hasText(summary),Article::getSummary,summary)
                //置顶的文章要显示在最前面
                .orderByAsc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<Article> articleList = page.getRecords();

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList,ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(Objects.nonNull(category)){
            articleDetailVo.setCategoryName(category.getName());
        }

        //查询文章对应的tag列表
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<Long> tags = articleTagService.list(wrapper).stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        articleDetailVo.setTags(tags);
        //封装响应，返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应的文章浏览量
        redisCache.incrementCacheValue(SystemConstants.REDIS_VIEW_COUNT_KEY_PREFIX,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(ArticleDto articleDto) {
        //添加博客文章
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        save(article);
        //建立标签和文章之间的关系
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                                        .map(tagId -> new ArticleTag(article.getId(),tagId))
                                        .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleDto articleDto) {
        //更新博客文章
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        updateById(article);
        //更新标签和文章之间的关系
        //article_tag 先删除，再添加
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(wrapper);
        //建立标签和文章之间的关系
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteArticle(String id) {
        if(!StringUtils.hasText(id)){
            throw new SystemException(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        List<Long> ids = Arrays.stream(id.split(","))
                .map(s -> Long.valueOf(s))
                .collect(Collectors.toList());
        //删除文章记录
        removeByIds(ids);

        //删除文章和标签的对应关系
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArticleTag::getArticleId,ids);
        articleTagService.remove(wrapper);

        //TODO 删除文章下的所有评论


        return ResponseResult.okResult();
    }

    @Override
    public void updateBatchViewCount(List<Article> articles) {
        for (Article article : articles){
            updateSingleViewCount(article);
        }
    }

    private void updateSingleViewCount(Article article){
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Article::getViewCount, article.getViewCount())
                .eq(Article::getId,article.getId());
        update(null,wrapper);
    }


    private Integer getViewCountFromRedis(Long id){
        return redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY_PREFIX, id.toString());
    }

}
