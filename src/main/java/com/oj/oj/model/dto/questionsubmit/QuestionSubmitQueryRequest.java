package com.oj.oj.model.dto.questionsubmit;

import java.io.Serializable;

import com.oj.oj.common.PageRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 *
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目Id
     */
    private Long questionid;

    /**
     * 用户ID
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}