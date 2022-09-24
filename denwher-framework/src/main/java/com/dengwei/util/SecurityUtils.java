package com.dengwei.util;

import com.dengwei.domain.entity.LoginUser;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.exception.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Denwher
 * @version 1.0
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
//        Object principal = getAuthentication().getPrincipal();
//        return principal instanceof LoginUser ? (LoginUser) principal : ;
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}
