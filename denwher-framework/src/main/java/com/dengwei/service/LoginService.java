package com.dengwei.service;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.entity.User;

/**
 * @author Denwher
 * @version 1.0
 */
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getInfo();
}
