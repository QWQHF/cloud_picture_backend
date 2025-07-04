package com.hf.cloudpicture.aop;

import com.hf.cloudpicture.annotation.AuthCheck;
import com.hf.cloudpicture.exception.BusinessException;
import com.hf.cloudpicture.exception.ErrorCode;
import com.hf.cloudpicture.modle.entity.User;
import com.hf.cloudpicture.modle.enums.UserRoleEnum;
import com.hf.cloudpicture.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须的权限，转换为枚举
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 不需要权限
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // 必须有该权限
        // 获取当前用户权限，转换为枚举
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        // 无权限
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 需要管理员权限，用户没有管理员权限
        if (userRoleEnum != UserRoleEnum.ADMIN && mustRoleEnum.equals(UserRoleEnum.ADMIN)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 拥有权限，放行
        return joinPoint.proceed();
    }
}
