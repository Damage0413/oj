package com.oj.oj.judge.codesandbox.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteCodeRequest {

    /**
     * 输入测试用例
     */
    private List<String> inputList;

    /**
     * 输入代码
     */
    private String code;

    /**
     * 使用的语言
     */
    private String language;
}
