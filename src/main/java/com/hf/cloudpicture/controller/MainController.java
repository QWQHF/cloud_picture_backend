package com.hf.cloudpicture.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.hf.cloudpicture.common.BaseResponse;
import com.hf.cloudpicture.common.ResultUtils;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     *
     * @return
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
