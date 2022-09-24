package com.dengwei.domain.vo;

import com.dengwei.domain.dto.UserDto;
import com.dengwei.domain.entity.Role;
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
public class UserSystemVo {
    /**
     * roleIds：用户所关联的角色id列表
     */
    private List<Long> roleIds;

    /**
     * roles：所有角色的列表
     */
    private List<Role> roles;

    /**
     * user：用户信息
     */
    private UserDto user;

    /*
		{
		"roleIds":[
			"11"
		],
		"roles":[
			{
				"createBy":"0",
				"createTime":"2021-11-12 18:46:19",
				"delFlag":"0",
				"id":"1",
				"remark":"超级管理员",
				"roleKey":"admin",
				"roleName":"超级管理员",
				"roleSort":"1",
				"status":"0",
				"updateBy":"0"
			},
			{
				"createBy":"0",
				"createTime":"2021-11-12 18:46:19",
				"delFlag":"0",
				"id":"2",
				"remark":"普通角色",
				"roleKey":"common",
				"roleName":"普通角色",
				"roleSort":"2",
				"status":"0",
				"updateBy":"0",
				"updateTime":"2022-01-02 06:32:58"
			},
			{
				"createTime":"2022-01-06 22:07:40",
				"delFlag":"0",
				"id":"11",
				"remark":"嘎嘎嘎",
				"roleKey":"aggag",
				"roleName":"嘎嘎嘎",
				"roleSort":"5",
				"status":"0",
				"updateBy":"1",
				"updateTime":"2022-09-11 20:34:49"
			},
			{
				"createTime":"2022-01-16 14:49:30",
				"delFlag":"0",
				"id":"12",
				"roleKey":"link",
				"roleName":"友链审核员",
				"roleSort":"1",
				"status":"0",
				"updateTime":"2022-01-16 16:05:09"
			}
		],
		"user":{
			"email":"weq@2132.com",
			"id":"14787164048663",
			"nickName":"sg777",
			"sex":"0",
			"status":"0",
			"userName":"sg777"
		}
	}
     */
}
