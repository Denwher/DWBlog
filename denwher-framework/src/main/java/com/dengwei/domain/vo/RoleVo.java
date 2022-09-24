package com.dengwei.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    /*{
        "id":"12",
        "roleKey":"link",
        "roleName":"友链审核员",
        "roleSort":"1",
        "status":"0"
    }*/
    //id
    private Long id;
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    // 备注
    private String remark;

}
