package com.dengwei.service.impl;

import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.LoginUser;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.vo.AdminUserInfoVo;
import com.dengwei.domain.vo.BlogUserLoginVo;
import com.dengwei.domain.vo.RoutersVo;
import com.dengwei.domain.vo.UserInfoVo;
import com.dengwei.service.BlogLoginService;
import com.dengwei.service.LoginService;
import com.dengwei.service.MenuService;
import com.dengwei.service.RoleService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.JwtUtil;
import com.dengwei.util.RedisCache;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveListCommands;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断authenticate是否正常返回，即是否通过认证
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误！");
        }
        //获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //将用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_USER_LOGIN_KEY_PREFIX + userId, loginUser);
        //生成token，并返回
        String jwt = JwtUtil.createJWT(userId.toString());
        Map<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //退出登录，就是让redis里面的token失效
        //获取userId
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.REDIS_USER_LOGIN_KEY_PREFIX + userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo() {
        //获取当前登录用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户权限信息
        List<String> permissions = menuService.getPermissionByUserId(userId);
        //根据用户id查询用户角色信息
        List<String> roleKeys = roleService.getRoleKeysByUserId(userId);

        //获取用户信息
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(new AdminUserInfoVo(permissions,roleKeys,userInfoVo));
    }
}
