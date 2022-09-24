package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.RoutersVo;
import com.dengwei.exception.SystemException;
import com.dengwei.service.BlogLoginService;
import com.dengwei.service.LoginService;
import com.dengwei.service.MenuService;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //前端必须要传入用户名，才能进行后面的程序，否则抛出异常
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        return loginService.getInfo();
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        //查询menu，结果是tree的形式
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.getRouterMenuTreeByUserId(userId);
        //封装数据返回
        RoutersVo routersVo = new RoutersVo(menus);
        return ResponseResult.okResult(routersVo);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
