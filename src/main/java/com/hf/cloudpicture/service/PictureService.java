package com.hf.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hf.cloudpicture.modle.dto.picture.PictureQueryRequest;
import com.hf.cloudpicture.modle.dto.picture.PictureReviewRequest;
import com.hf.cloudpicture.modle.dto.picture.PictureUploadRequest;
import com.hf.cloudpicture.modle.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hf.cloudpicture.modle.entity.User;
import com.hf.cloudpicture.modle.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author HF
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-07-06 16:43:16
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     * @param inputSource
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取查询对象
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取封装类
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 分页获取图片封装类
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 校验
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);
}
