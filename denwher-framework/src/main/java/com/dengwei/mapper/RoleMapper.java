package com.dengwei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dengwei.domain.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-30 09:54:33
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select distinct r.role_key from sys_user_role ur, sys_role r " +
            "where " +
            "ur.user_id = 2 and ur.role_id = r.id and r.del_flag = 0 and r.`status` = 0")
    List<String> queryRoleKeysByUserId(Long userId);

}
