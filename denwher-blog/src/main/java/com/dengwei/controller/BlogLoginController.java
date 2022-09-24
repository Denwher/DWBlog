package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.exception.SystemException;
import com.dengwei.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //前端必须要传入用户名，才能进行后面的程序，否则抛出异常
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
