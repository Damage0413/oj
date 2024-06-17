package com.oj.oj.judge.codesandbox.model;

import java.util.List;

import com.oj.oj.model.dto.questionsubmit.JudgeInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteCodeResponse {
    /**
     * 测试用例输出
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
