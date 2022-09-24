package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.RoleDto;
import com.dengwei.domain.entity.Role;
import com.dengwei.domain.entity.RoleMenu;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.MenuTreeVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.domain.vo.RoleVo;
import com.dengwei.mapper.RoleMapper;
import com.dengwei.service.MenuService;
import com.dengwei.service.RoleMenuService;
import com.dengwei.service.RoleService;
import com.dengwei.util.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-08-30 09:54:33
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;
    

    @Override
    public List<String> getRoleKeysByUserId(Long userId) {
        //如果id为1，直接返回admin的数组就ok
        if(userId == 1L){
            return new ArrayList<String>(Collections.singletonList("admin"));
        }
        //否则，返回对应的角色
        return roleMapper.queryRoleKeysByUserId(userId);
    }

    @Override
    public ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize,
                                              String roleName, String status) {
        //添加搜索条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName)
                .like(StringUtils.hasText(status),Role::getStatus,status)
                .orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page, wrapper);
        List<Role> roles = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleDto roleDto) {
        if(!StringUtils.hasText(roleDto.getRoleName()) ||
                !StringUtils.hasText(roleDto.getRoleKey()) ||
                Objects.isNull(roleDto.getRoleSort())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        // role 表中添加记录
        try {
            Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
            save(role);
            // role-menu 表中添加记录，建立角色和menu之间的关系
            List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                    .map(menuId -> new RoleMenu(role.getId(), menuId))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getById(id);
        if(Objects.isNull(role)){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult changeStatus(Map<String, String> map) {
        if(Objects.isNull(map) || map.isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        String roleId = map.get("roleId");
        String status = map.get("status");
        boolean success = update().eq(StringUtils.hasText(roleId), "id", roleId)
                .setSql(StringUtils.hasText(status), "status = " + status).update();
        if(!success){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleDto roleDto) {
        try {
            Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
            // 更新role表
            updateById(role);
            // 更新role-menu表
            // 1.根据roleId删除role-menu的关联
            LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleMenu::getRoleId,role.getId());
            roleMenuService.remove(wrapper);
            // 2.建立role-menu新的关联
            List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                    .map(menuId -> new RoleMenu(role.getId(), menuId))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseResult deleteRole(String id) {
        try {
            List<Long> roleIds = Arrays.stream(id.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            // 删除 role 表中的记录
            removeByIds(roleIds);
            // 删除 role-menu 表中的记录
            roleIds.stream().forEach(new Consumer<Long>() {
                @Override
                public void accept(Long roleId) {
                    LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(RoleMenu::getRoleId,roleId);
                    roleMenuService.remove(wrapper);
                }
            });
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public List<Role> listAllRole() {
        return lambdaQuery().eq(Role::getStatus, 0).list();
    }
}
