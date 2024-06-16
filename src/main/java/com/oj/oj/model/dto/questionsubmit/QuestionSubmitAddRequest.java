package com.oj.oj.model.dto.questionsubmit;

import java.io.Serializable;

import lombok.Data;

/**
 * 创建请求
 *
 * 
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;
}