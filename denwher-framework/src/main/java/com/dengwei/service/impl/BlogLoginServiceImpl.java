package com.dengwei.service.impl;

import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.LoginUser;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.vo.BlogUserLoginVo;
import com.dengwei.domain.vo.UserInfoVo;
import com.dengwei.service.BlogLoginService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.JwtUtil;
import com.dengwei.util.RedisCache;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断authenticate是否正常返回，即是否通过认证
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误！");
        }
        //获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //将用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN_KEY_PREFIX + userId, loginUser);
        //生成token
        String jwt = JwtUtil.createJWT(userId.toString());
        //把token和userinfo封装 返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //退出登录，就是让redis里面的token失效
        //获取userId
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN_KEY_PREFIX + userId);
        return ResponseResult.okResult();
    }
}
