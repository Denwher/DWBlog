package com.dengwei.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加/更新博客文章dto")
public class ArticleDto {
    /*{
    "title":"测试新增博文",
    "thumbnail":"https://sg-blog-oss.oss-cn-beijing.aliyuncs.com/2022/08/21/4ceebc07e7484beba732f12b0d2c43a9.png",
    "isTop":"0",
    "isComment":"0",
    "content":"# 一级标题\n## 二级标题\n![Snipaste_20220228_224837.png](https://sg-blog-oss.oss-cn-beijing.aliyuncs.com/2022/08/21/c3af554d4a0f4935b4073533a4c26ee8.png)\n正文",
    "tags":[
        1,
        4
    ],
    "categoryId":1,
    "summary":"哈哈",
    "status":"1"
    }*/

    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    private Long viewCount;
    //是否允许评论 1是，0否
    private String isComment;
    private List<Long> tags;
}
