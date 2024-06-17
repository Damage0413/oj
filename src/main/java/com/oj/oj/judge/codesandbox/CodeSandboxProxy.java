package com.oj.oj.judge.codesandbox;

import com.oj.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.oj.oj.judge.codesandbox.model.ExecuteCodeResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理，代替原本的代码沙箱接口
 */
@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandbox {
    private CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }

}
