package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Comment;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.CommentVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.exception.SystemException;
import com.dengwei.handler.mybatisplus.MyMetaObjectHandler;
import com.dengwei.mapper.CommentMapper;
import com.dengwei.service.CommentService;
import com.dengwei.service.UserService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-08-26 23:51:20
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult getCommentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        //获取当前文章的所有根评论
        //如果是文章下的评论，就加入articleId作为查询条件
        wrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type),Comment::getArticleId,articleId)
                //获取根评论: rootId=-1
                .eq(Comment::getRootId, SystemConstants.ROOT_COMMENT)
                .eq(Comment::getType,type);
        //进行分页查询
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page, wrapper);
        //为toCommentUserName, username属性赋值
        List<CommentVo> commentVos = toCommentList(page.getRecords());
        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for(CommentVo commentVo : commentVos){
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_CANNOT_EMPTY);
        }
        save(comment);
        //{"articleId":1,
        // "type":0,
        // "rootId":-1,
        // "toCommentId":-1,
        // "toCommentUserId":-1,
        // "content":"评论了文章"}
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {
        //查询根评论下的子评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getRootId,id).orderByAsc(Comment::getCreateTime);
        return toCommentList(list(wrapper));
    }

    private List<CommentVo> toCommentList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //为toCommentUserName, username属性赋值
        for(CommentVo commentVo : commentVos){
            //通过createBy查询用户的昵称并赋值
            User user = userService.getById(commentVo.getCreateBy());
            commentVo.setUsername(user.getNickName());
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询, 如果为-1，说明是根评论
            if(commentVo.getToCommentUserId() != -1){
                User toCommentUser = userService.getById(commentVo.getToCommentUserId());
                commentVo.setToCommentUserName(toCommentUser.getNickName());
            }
        }
        return commentVos;
    }
}
