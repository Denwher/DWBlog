package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.RoleDto;
import com.dengwei.domain.entity.Role;
import com.dengwei.domain.vo.PageVo;

import java.util.List;
import java.util.Map;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-08-30 09:54:33
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeysByUserId(Long userId);

    ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult getRole(Long id);

    ResponseResult changeStatus(Map<String, String> map);

    ResponseResult updateRole(RoleDto roleDto);

    ResponseResult deleteRole(String id);

    List<Role> listAllRole();


}
