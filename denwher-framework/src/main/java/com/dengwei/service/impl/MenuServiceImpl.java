package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.entity.Role;
import com.dengwei.domain.entity.RoleMenu;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.MenuTreeVo;
import com.dengwei.domain.vo.MenuVo;
import com.dengwei.mapper.MenuMapper;
import com.dengwei.service.MenuService;
import com.dengwei.service.RoleMenuService;
import com.dengwei.service.RoleService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-08-30 09:53:45
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> getPermissionByUserId(Long userId) {
        //如果是管理员, id=1, 返回所有的权限信息,
        if(userId == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //展示所有的按钮, 菜单
            wrapper.in(Menu::getMenuType, SystemConstants.BUTTON,SystemConstants.MENU)
                    //状态正常的
                    .eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        //否则返回具有的权限，getBaseMapper()作用是拿到一个mapper对象，也可以@Autowird注入
        return //getBaseMapper().queryPermsByUserId(userId);
                menuMapper.queryPermsByUserId(userId);
    }

    @Override
    public List<Menu> getRouterMenuTreeByUserId(Long userId){
        List<Menu> menus;
        //判断是否为管理员账户
        if(SecurityUtils.isAdmin()){
            //如果是管理员，返回所有符合要求的menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则返回对应的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<Menu> getMenuListByCondition(String menuName, String status) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //like默认是“%值%”的方式匹配
        //如果是“%值”和“值%”，可以用like
        //判断menuName和status，不为空且“”才能加入查询条件
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName)
                .like(StringUtils.hasText(status),Menu::getStatus,status)
                //菜单列表根据orderNum属性进行排序
                .orderByDesc(Menu::getOrderNum);
        return list(wrapper);
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        if(Objects.isNull(menu)){
            ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        try {
            updateById(menu);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        // 必填项不能为空
        if(!StringUtils.hasText(menu.getMenuName()) ||
            !StringUtils.hasText(menu.getPath()) ||
            !Objects.isNull(menu.getOrderNum())){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        if(Objects.isNull(id)){
            ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<MenuTreeVo> treeSelect() {
        // 查找所有菜单
        List<MenuTreeVo> menuTreeVos = menuMapper.selectMenuTree();
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTree = menuTreeVos.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> {
                    List<MenuTreeVo> children = getChildren(menuTreeVos, menu);
                    return menu.setChildren(children);
                }).collect(Collectors.toList());
        return menuTree;
    }

    @Override
    public ResponseResult getRoleMenuTreeselect(Long id) {
        Role role = roleService.getById(id);
        if(Objects.isNull(role)){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        HashMap<String, List> map = new HashMap<>();
        // 获取role-menu表中的记录
        List<Long> menuIds = roleMenuService.list().stream()
                .filter(roleMenu -> role.getId().equals(roleMenu.getRoleId()))
                .map(RoleMenu::getMenuId).collect(Collectors.toList());
        map.put("checkedKeys",menuIds);
        // 获取菜单树
        List<MenuTreeVo> menuTreeVos = treeSelect();
        map.put("menus",menuTreeVos);
        return ResponseResult.okResult(map);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {
                    List<Menu> children = getChildren(menus, menu);
                    return menu.setChildren(children);
                }).collect(Collectors.toList());
        return menuTree;
    }

    /**
     * @param menus: 全部菜单集合
     * @param menu: 子菜单的父菜单
      * @return List<Menu>
     * @author Denwher
     * @description 获取存入参数的子menu集合
     * @date 2022/8/31 16:27
     */
    private List<Menu> getChildren(List<Menu> menus, Menu menu) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //考虑到子菜单还会有子菜单，所以此处还需要继续递归调用
                .map(m -> m.setChildren(getChildren(menus,m)))
                .collect(Collectors.toList());
        return childrenList;
    }


    private List<MenuTreeVo> getChildren(List<MenuTreeVo> menus, MenuTreeVo menu) {
        List<MenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //考虑到子菜单还会有子菜单，所以此处还需要继续递归调用
                .map(m -> m.setChildren(getChildren(menus,m)))
                .collect(Collectors.toList());
        return childrenList;
    }

}
