package com.dengwei.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    /*
    {
    "roleName":"测试新增角色",
    "roleKey":"wds",
    "roleSort":0,
    "status":"0",
    "menuIds":[
        "1",
        "100"
    ],
    "remark":"我是角色备注"
    }
     */

    private Long id;
    // 角色名
    private String roleName;
    // 角色权限字符串
    private String roleKey;
    // 显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;

    // menu的id
    private List<Long> menuIds;


}
