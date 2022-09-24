package com.dengwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.domain.entity.UserRole;
import com.dengwei.mapper.UserRoleMapper;
import com.dengwei.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-09-23 14:10:14
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
