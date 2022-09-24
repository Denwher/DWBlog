package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.vo.MenuTreeVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-08-30 09:53:45
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermissionByUserId(Long userId);

    List<Menu> getRouterMenuTreeByUserId(Long userId);

    List<Menu> getMenuListByCondition(String menuName, String status);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult addMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    List<MenuTreeVo> treeSelect();

    ResponseResult getRoleMenuTreeselect(Long id);
}
