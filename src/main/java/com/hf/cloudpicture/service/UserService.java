package com.hf.cloudpicture.service;

import com.hf.cloudpicture.modle.dto.UserRegisterRequest;
import com.hf.cloudpicture.modle.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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

    String getEncryptPassword(String userPassword);
}
