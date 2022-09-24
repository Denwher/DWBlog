package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.TagListDto;
import com.dengwei.domain.entity.Tag;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.exception.SystemException;
import com.dengwei.service.TagService;
import com.dengwei.util.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.getTagPageList(pageNum,pageSize,tagListDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.getAllTagList();
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagListDto tagListDto){
        return tagService.updateTag(tagListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") String id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable("id") Integer id){
        return tagService.getTagById(id);
    }
}
