package com.hf.cloudpicture.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hf.cloudpicture.exception.BusinessException;
import com.hf.cloudpicture.exception.ErrorCode;
import com.hf.cloudpicture.modle.dto.UserQueryRequest;
import com.hf.cloudpicture.modle.entity.User;
import com.hf.cloudpicture.modle.enums.UserRoleEnum;
import com.hf.cloudpicture.mapper.UserMapper;
import com.hf.cloudpicture.modle.vo.LoginUserVO;
import com.hf.cloudpicture.modle.vo.UserVO;
import com.hf.cloudpicture.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hf.cloudpicture.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author HF
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-07-04 15:23:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        //检验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //检查用户账号是否和数据库中已有的重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号重复");
        }
        //密码一定要加密
        String encryptPassword = getEncryptPassword(userPassword);
        //插入到数据库中
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        try {
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
            }
        } catch (Exception e) {
            log.error("注册失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //检验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        //对用户传递的密码进行加密
        String encryptPassword = getEncryptPassword(userPassword);
        //查询数据库中的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        //保存用户的登陆态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null){
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser =  this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取加密密码
     *
     * @param userPassword
     * @return
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 加盐
        final String SALT = "hf";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        //判断用户是否登陆
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前用户（脱敏）
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前用户列表（脱敏）
     *
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id),"id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortOrder), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



}




