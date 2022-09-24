package com.dengwei.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.TagListDto;
import com.dengwei.domain.entity.Tag;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.domain.vo.TagVo;
import com.dengwei.exception.SystemException;
import com.dengwei.mapper.TagMapper;
import com.dengwei.service.TagService;
import com.dengwei.util.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-08-29 22:19:54
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> getTagPageList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        Page<Tag> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName, tagListDto.getName())
                .eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        page(page,wrapper);
        // 封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        Tag tag = BeanCopyUtils.copyBean(tagListDto,Tag.class);
        //标签内容和描述不能为空
        if(!StringUtils.hasText(tag.getName()) && !StringUtils.hasText(tag.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_CANNOT_EMPTY);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(String id) {
        if(!StringUtils.hasText(id)){
            throw new SystemException(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        List<Long> ids = Arrays.stream(id.split(","))
                                    .map(s -> Long.valueOf(s))
                                    .collect(Collectors.toList());
        //使用逻辑删除
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Integer id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag,TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagListDto tagListDto) {
        Tag tag = BeanCopyUtils.copyBean(tagListDto,Tag.class);
        //标签内容和描述不能为空
        if(!StringUtils.hasText(tag.getName()) && !StringUtils.hasText(tag.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_CANNOT_EMPTY);
        }
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllTagList() {
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(list(), TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }
}
