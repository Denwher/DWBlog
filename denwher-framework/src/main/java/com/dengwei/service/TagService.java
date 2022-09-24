package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.TagListDto;
import com.dengwei.domain.entity.Tag;
import com.dengwei.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-08-29 22:19:54
 */
public interface TagService extends IService<Tag> {
    
    ResponseResult<PageVo> getTagPageList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult deleteTag(String id);

    ResponseResult getTagById(Integer id);

    ResponseResult updateTag(TagListDto tagListDto);

    ResponseResult getAllTagList();
}
