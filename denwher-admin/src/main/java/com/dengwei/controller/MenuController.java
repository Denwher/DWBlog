package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.MenuTreeVo;
import com.dengwei.domain.vo.MenuVo;
import com.dengwei.service.MenuService;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/treeselect")
    public ResponseResult<List<MenuTreeVo>> treeSelect(){
        List<MenuTreeVo> menuTreeVos = menuService.treeSelect();
        return ResponseResult.okResult(menuTreeVos);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTreeselect(@PathVariable("id") Long id){
        if(Objects.isNull(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return menuService.getRoleMenuTreeselect(id);
    }

    @GetMapping("/list")
    public ResponseResult menuList(String menuName, String status){
        if(!SecurityUtils.isAdmin()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        List<Menu> menuList = menuService.getMenuListByCondition(menuName,status);
        return ResponseResult.okResult(menuList);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id") Long id){
        if(Objects.isNull(id)){
            ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return menuService.getMenuById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        if(Objects.isNull(menu)){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return menuService.updateMenu(menu);
    }

    @PostMapping
    public ResponseResult saveMenu(@RequestBody Menu menu){
        if(Objects.isNull(menu)){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return menuService.addMenu(menu);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteMenuById(@PathVariable("id") Long id){
        return menuService.deleteMenu(id);
    }
}
