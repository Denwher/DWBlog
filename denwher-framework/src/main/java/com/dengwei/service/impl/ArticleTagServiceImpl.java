package com.dengwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.domain.entity.ArticleTag;
import com.dengwei.mapper.ArticleTagMapper;
import com.dengwei.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-08 17:08:41
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
