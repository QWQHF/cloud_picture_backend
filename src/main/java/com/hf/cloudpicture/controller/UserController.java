package com.hf.cloudpicture.controller;

import com.hf.cloudpicture.common.BaseResponse;
import com.hf.cloudpicture.common.ResultUtils;
import com.hf.cloudpicture.exception.ErrorCode;
import com.hf.cloudpicture.exception.ThrowUtils;
import com.hf.cloudpicture.modle.dto.UserRegisterRequest;
import com.hf.cloudpicture.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }
}
