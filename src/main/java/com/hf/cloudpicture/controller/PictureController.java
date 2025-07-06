package com.hf.cloudpicture.controller;

import com.hf.cloudpicture.annotation.AuthCheck;
import com.hf.cloudpicture.common.BaseResponse;
import com.hf.cloudpicture.common.ResultUtils;
import com.hf.cloudpicture.constant.UserConstant;
import com.hf.cloudpicture.modle.dto.picture.PictureUploadRequest;
import com.hf.cloudpicture.modle.entity.User;
import com.hf.cloudpicture.modle.vo.PictureVO;
import com.hf.cloudpicture.service.PictureService;
import com.hf.cloudpicture.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, HttpServletRequest  request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }
}
