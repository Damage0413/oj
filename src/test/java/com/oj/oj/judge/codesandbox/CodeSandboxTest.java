package com.oj.oj.judge.codesandbox;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.oj.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.oj.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.oj.oj.model.enums.QuestionSubmitLanguageEnum;

@SpringBootTest
public class CodeSandboxTest {

    @Value("${codesandbox.type}")
    private String type;

    @Test
    void testExecuteCode() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
