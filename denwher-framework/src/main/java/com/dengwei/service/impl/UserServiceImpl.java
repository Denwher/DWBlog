package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.UserDto;
import com.dengwei.domain.entity.Role;
import com.dengwei.domain.entity.RoleMenu;
import com.dengwei.domain.entity.User;
import com.dengwei.domain.entity.UserRole;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.domain.vo.UserInfoVo;
import com.dengwei.domain.vo.UserSystemVo;
import com.dengwei.exception.SystemException;
import com.dengwei.mapper.UserMapper;
import com.dengwei.service.RoleService;
import com.dengwei.service.UserRoleService;
import com.dengwei.service.UserService;
import com.dengwei.util.BeanCopyUtils;
import com.dengwei.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-08-27 01:36:20
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult getUserInfo() {
        User user = getById(SecurityUtils.getUserId());
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        /*
        {
          "email": "string",
          "nickName": "string",
          "password": "string",
          "userName": "string"
        }
        */
        //对User进行属性的非空校验
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // 前台用户设置为普通用户
        user.setType(SystemConstants.NOT_ADMIN);
        // 存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserDto userDto) {
        String phoneNumber = userDto.getPhonenumber();
        String userName = userDto.getUserName();
        String status = userDto.getStatus();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(phoneNumber), User::getPhonenumber, phoneNumber)
                .like(StringUtils.hasText(userName), User::getUserName, userName)
                .like(StringUtils.hasText(status), User::getStatus, status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<User> userList = page.getRecords();
        return ResponseResult.okResult(new PageVo(userList,page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addSystemUser(UserDto userDto) {
        /*
        新增用户时可以直接关联角色。
        注意：新增用户时注意密码加密存储。
        用户名不能为空，否则提示：必需填写用户名
        用户名必须之前未存在，否则提示：用户名已存在
        手机号必须之前未存在，否则提示：手机号已存在
        邮箱必须之前未存在，否则提示：邮箱已存在
         */
        // 用户名不能为空，否则提示：必需填写用户名
        if(!StringUtils.hasText(userDto.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        // 用户名必须之前未存在，否则提示：用户名已存在
        if(userNameExist(userDto.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 手机号必须之前未存在，否则提示：手机号已存在
        if(phoneNumberExist(userDto.getPhonenumber())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONE_NUMBER_EXIST);
        }
        // 邮箱必须之前未存在，否则提示：邮箱已存在
        if(emailExist(userDto.getEmail())){
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }

        try {
            User user = BeanCopyUtils.copyBean(userDto, User.class);
            //对密码进行加密
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            // 后台用户设置为管理用户
            user.setType(SystemConstants.ADMIN);
            // user 表中添加记录
            save(user);
            // user-role 表中添加记录，建立user和role之间的关系
            List<UserRole> userRoles = userDto.getRoleIds().stream()
                    .map(roleId -> new UserRole(user.getId(), roleId))
                    .collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult getUser(Long id) {
        // 用户信息
        User user = getById(id);
        if(Objects.isNull(user)){
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_IS_NULL);
        }
        UserDto userVo = BeanCopyUtils.copyBean(user, UserDto.class);
        // 所有角色的列表
        List<Role> roles = roleService.listAllRole();
        // 用户所关联的角色id列表
        List<Long> roleIds = userRoleService.lambdaQuery().eq(UserRole::getUserId, id)
                .list().stream().map(UserRole::getRoleId)
                .collect(Collectors.toList());
        UserSystemVo userSystemVo = new UserSystemVo(roleIds, roles, userVo);
        return ResponseResult.okResult(userSystemVo);
    }

    @Override
    @Transactional
    public ResponseResult updateSystemUser(UserDto userDto) {
        try {
            User user = BeanCopyUtils.copyBean(userDto, User.class);
            // 更新user表
            updateById(user);
            // 更新user-role表，先删除，再重新建立
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserRole::getUserId, user.getId());
            userRoleService.remove(wrapper);
            List<UserRole> userRoles = userDto.getRoleIds().stream()
                    .map(roleId -> new UserRole(user.getId(), roleId))
                    .collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseResult deleteUser(String id) {
        List<Long> userIds = Arrays.stream(id.split(","))
                .map(s -> Long.valueOf(s))
                .collect(Collectors.toList());
        // 删除user表的记录
        removeByIds(userIds);
        // 删除 user-role 表中的记录
        userIds.stream().forEach(new Consumer<Long>() {
            @Override
            public void accept(Long userId) {
                LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UserRole::getUserId, userId);
                userRoleService.remove(wrapper);
            }
        });
        return null;
    }

    @Override
    public ResponseResult changeStatus(Map<String, String> map) {
        if(Objects.isNull(map) || map.isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        String roleId = map.get("roleId");
        String status = map.get("status");
        boolean success = update().eq(StringUtils.hasText(roleId), "id", roleId)
                .setSql(StringUtils.hasText(status), "status = " + status).update();
        if(!success){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    private boolean phoneNumberExist(String phonenumber) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhonenumber, phonenumber);
        User user = getOne(wrapper);
        return Objects.nonNull(user);
    }

    private boolean userNameExist(String username){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = getOne(wrapper);
        return Objects.nonNull(user);
    }

    private boolean nickNameExist(String nickname){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName, nickname);
        User user = getOne(wrapper);
        return Objects.nonNull(user);
    }

    private boolean emailExist(String email){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = getOne(wrapper);
        return Objects.nonNull(user);
    }
}
