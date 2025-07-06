package com.hf.cloudpicture.modle.vo;

import lombok.Data;

import java.util.List;

/**
 * 图片标签类别
 */
@Data
public class PictureTagCategory {
    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private List<String> categoryList;
}
