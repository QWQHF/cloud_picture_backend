package com.hf.cloudpicture.common;

import com.hf.cloudpicture.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    private T date;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.date = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
