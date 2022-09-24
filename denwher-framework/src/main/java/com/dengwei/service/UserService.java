package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.UserDto;
import com.dengwei.domain.entity.User;

import java.util.Map;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-08-27 01:36:20
 */
public interface UserService extends IService<User> {
    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, UserDto userDto);

    ResponseResult addSystemUser(UserDto userDto);

    ResponseResult getUser(Long id);

    ResponseResult updateSystemUser(UserDto userDto);

    ResponseResult deleteUser(String id);

    ResponseResult changeStatus(Map<String, String> map);

}
