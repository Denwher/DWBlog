package com.dengwei.filter;

import com.alibaba.fastjson.JSON;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.LoginUser;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.util.JwtUtil;
import com.dengwei.util.RedisCache;
import com.dengwei.util.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Denwher
 * @version 1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = httpServletRequest.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明当前访问不需要登录或未登录状态，放行去登录或直接访问
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析获取的token，获取id
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //说明token超时或者非法
            //响应告诉前端重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
        }
        String userId = claims.getSubject();
        //到redis中获取用户信息，存入SecurityContextHolder
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.REDIS_USER_LOGIN_KEY_PREFIX + userId);
        //如果获取不到loginUser说明过期了
        if(Objects.isNull(loginUser)){
            //响应告诉前端重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
        }

        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
