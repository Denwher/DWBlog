package com.dengwei.service.impl;

import com.dengwei.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@Service("ps")
public class PermissionService {

    public boolean hasPermission(String permission){
        //如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则，获取当前登录用户所具有的权限列表
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
