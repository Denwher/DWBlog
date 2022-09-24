package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.entity.LoginUser;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.mapper.MenuMapper;
import com.dengwei.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //通过用户名，从数据库中查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,s);
        User user = userMapper.selectOne(wrapper);
        //判断是否查到用户，没有查到则抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException(AppHttpCodeEnum.USER_IS_NULL.getMsg());
        }
        //返回用户信息
        //查询权限信息封装到loginUser中
        //前台用户不需要权限，后台用户才需要
        if(SystemConstants.ADMIN.equals(user.getType())){
            List<String> perms = menuMapper.queryPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }
        return new LoginUser(user,null);
    }
}
