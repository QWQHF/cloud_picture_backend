package com.hf.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hf.cloudpicture.modle.dto.space.SpaceQueryRequest;
import com.hf.cloudpicture.modle.dto.space.SpaceAddRequest;
import com.hf.cloudpicture.modle.entity.Picture;
import com.hf.cloudpicture.modle.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hf.cloudpicture.modle.entity.User;
import com.hf.cloudpicture.modle.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author HF
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-07-11 22:24:33
*/
public interface SpaceService extends IService<Space> {

    /**
     * 校验
     */
    public void validSpace(Space space, boolean add);

    /**
     * 根据空间级别填充限额
     */
    public void fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 获取查询对象
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 获取封装类
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 分页获取空间封装类
     * @param spacePage
     * @param request
     * @return
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);


}
