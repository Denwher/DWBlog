package com.dengwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.domain.entity.RoleMenu;
import com.dengwei.mapper.RoleMenuMapper;
import com.dengwei.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-09-22 23:05:55
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
