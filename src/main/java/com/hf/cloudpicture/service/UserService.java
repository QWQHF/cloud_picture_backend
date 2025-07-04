package com.hf.cloudpicture.service;

import com.hf.cloudpicture.modle.dto.UserRegisterRequest;
import com.hf.cloudpicture.modle.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hf.cloudpicture.modle.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HF
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-07-04 15:23:53
 */

public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登陆
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取加密密码
     *
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

}
