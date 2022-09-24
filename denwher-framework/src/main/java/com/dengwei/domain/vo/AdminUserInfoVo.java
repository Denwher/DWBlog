package com.dengwei.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserInfoVo {
    /**
     * 权限
     */
    private List<String> permissions;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 用户信息
     */
    private UserInfoVo user;
}
