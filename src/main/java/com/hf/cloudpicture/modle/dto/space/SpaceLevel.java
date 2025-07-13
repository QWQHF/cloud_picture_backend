package com.hf.cloudpicture.modle.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * 空间级别
 */
public class SpaceLevel {

    /**
     * 值
     */
    private int value;

    /**
     * 中文
     */
    private String text;

    /**
     * 最大图片数量
     */
    private long maxCount;

    /**
     * 最大容量
     */
    private long maxSize;
}
