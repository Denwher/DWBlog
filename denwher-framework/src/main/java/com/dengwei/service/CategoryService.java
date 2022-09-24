package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.CategoryDto;
import com.dengwei.domain.entity.Category;
import com.dengwei.domain.vo.PageVo;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-08-25 20:36:30
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult<PageVo> getPageList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult changeStatus(Long id, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult deleteCategory(String id);

    ResponseResult updateCategory(CategoryDto categoryDto);
}
