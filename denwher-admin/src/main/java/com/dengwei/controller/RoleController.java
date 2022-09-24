package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.RoleDto;
import com.dengwei.domain.entity.Role;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult<PageVo> roleList(Integer pageNum, Integer pageSize,
                                           String roleName, String status){
        return roleService.getRoleList(pageNum,pageSize,roleName,status);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roleList = roleService.listAllRole();
        return ResponseResult.okResult(roleList);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        if(Objects.isNull(roleDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return roleService.addRole(roleDto);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto){
        if(Objects.isNull(roleDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return roleService.updateRole(roleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Long id){
        if(Objects.isNull(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return roleService.getRole(id);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Map<String, String> map){
        return roleService.changeStatus(map);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") String id){
        if(!StringUtils.hasText(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return roleService.deleteRole(id);
    }

}
