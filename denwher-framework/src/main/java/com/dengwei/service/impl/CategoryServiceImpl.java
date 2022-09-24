package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.CategoryDto;
import com.dengwei.domain.entity.Article;
import com.dengwei.domain.entity.Category;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.CategoryVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.exception.SystemException;
import com.dengwei.mapper.CategoryMapper;
import com.dengwei.service.ArticleService;
import com.dengwei.service.CategoryService;
import com.dengwei.util.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-08-25 20:36:30
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //查询已发布文章的所属分类id，去重，得到分类id集合
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(wrapper);
        //通过转成set集合去重
        //Set<Long> categoryIds = list.stream().map(article -> article.getCategoryId())
        //.collect(Collectors.toSet());
        //通过distinct去重
        List<Long> categoryIds = list.stream().distinct().map(article -> article.getCategoryId())
                .collect(Collectors.toList());
        //根据categoryId获取Category集合
        List<Category> categories = listByIds(categoryIds);
        //获取状态为正常的分类
        categories = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装到vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categoryList = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<PageVo> getPageList(Integer pageNum, Integer pageSize, String name, String status) {
        //添加搜索条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Category::getName,name)
                .like(StringUtils.hasText(status),Category::getStatus,status);
        //设置分页
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        //封装数据并返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(),CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(Long id, String status) {
        //查询当前记录
        Category category = getById(id);
        category.setStatus(status);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        if(Objects.isNull(categoryDto)){
            throw new SystemException(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        if(!StringUtils.hasText(category.getName()) || !StringUtils.hasText(category.getDescription())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EMPTY);
        }
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        if(Objects.isNull(category)){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult deleteCategory(String id) {
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
    public ResponseResult updateCategory(CategoryDto categoryDto) {
        //对categoryDto参数校验
        if(Objects.isNull(categoryDto) ||
                !StringUtils.hasText(categoryDto.getName()) ||
                !StringUtils.hasText(categoryDto.getDescription()) ||
                !StringUtils.hasText("" + categoryDto.getPid())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        //更新友链
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }
}
