package com.dengwei.controller;

import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.CommentDto;
import com.dengwei.domain.entity.Comment;
import com.dengwei.service.CommentService;
import com.dengwei.util.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论功能", description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "文章评论列表", notes = "获取一页文章评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数")
    })
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.getCommentList(SystemConstants.ARTICLE_COMMENT, articleId,pageNum,pageSize);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表", notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.getCommentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }

    @PostMapping
    @ApiOperation(value = "添加评论", notes = "添加文章评论/友链评论")
    public ResponseResult addComment(@RequestBody CommentDto commentDto){
        Comment comment = BeanCopyUtils.copyBean(commentDto,Comment.class);
        return commentService.addComment(comment);
    }
}
