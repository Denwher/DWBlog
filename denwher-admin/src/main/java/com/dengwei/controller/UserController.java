package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.UserDto;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult userList(Integer pageNum, Integer pageSize, UserDto userDto){
        if(Objects.isNull(userDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return userService.getUserList(pageNum,pageSize,userDto);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserDto userDto){
        if(Objects.isNull(userDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return userService.addSystemUser(userDto);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UserDto userDto){
        if(Objects.isNull(userDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        return userService.updateSystemUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id") Long id){
        if(Objects.isNull(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") String id){
        if(!StringUtils.hasText(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        return userService.deleteUser(id);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Map<String, String> map){
        return userService.changeStatus(map);
    }
}
