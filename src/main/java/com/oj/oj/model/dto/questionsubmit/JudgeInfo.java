package com.oj.oj.model.dto.questionsubmit;

import lombok.Data;

/**
 * 判题信息
 * 
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 使用时间
     */
    private Long time;

    /**
     * 使用内存
     */
    private Long memory;

}
