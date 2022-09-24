package com.dengwei.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.CategoryDto;
import com.dengwei.domain.dto.LinkDto;
import com.dengwei.domain.entity.Category;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.ExcelCategoryVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.service.CategoryService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String name, String status){
        return categoryService.getPageList(pageNum,pageSize,name,status);
    }

    @GetMapping("/{id}/{status}")
    public ResponseResult changeStatus(@PathVariable("id") Long id,
                                       @PathVariable("status") String status){
        return categoryService.changeStatus(id,status);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteCategory(@PathVariable("id") String id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
