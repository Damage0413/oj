package com.oj.oj.judge;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.oj.oj.common.ErrorCode;
import com.oj.oj.exception.BusinessException;
import com.oj.oj.judge.codesandbox.CodeSandbox;
import com.oj.oj.judge.codesandbox.CodeSandboxFactory;
import com.oj.oj.judge.codesandbox.CodeSandboxProxy;
import com.oj.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.oj.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.oj.oj.judge.strategy.JudgeContext;
import com.oj.oj.model.dto.question.JudgeCase;
import com.oj.oj.model.dto.questionsubmit.JudgeInfo;
import com.oj.oj.model.entity.Question;
import com.oj.oj.model.entity.QuestionSubmit;
import com.oj.oj.model.enums.QuestionSubmitStatusEnum;
import com.oj.oj.service.QuestionService;
import com.oj.oj.service.QuestionSubmitService;

import cn.hutool.json.JSONUtil;

public class judgeServiceImpl implements judgeService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionSubmitService questionSubmitService;

    @Autowired
    private judgeManager judgeManager;

    @Value("${codesandbox.type}")
    private String type;

    @Override
    public QuestionSubmit doJudeg(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getPostId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）判断题目状态，如果不为“待判题”，则不重复执行
        Integer status = questionSubmit.getStatus();
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）修改数据库中题目状态
        QuestionSubmit questionSubmitupdate = new QuestionSubmit();
        questionSubmitupdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitupdate.setId(questionSubmitId);
        boolean updateById = questionSubmitService.updateById(questionSubmitupdate);
        if (!updateById) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInputString).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱结果设置题目的判题状态和信息
        // 此处使用策略模式开发,为了减少if语句使用manager，简化调用
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中题目的判题结果
        questionSubmitupdate = new QuestionSubmit();
        questionSubmitupdate.setId(questionSubmitId);
        questionSubmitupdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitupdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        updateById = questionSubmitService.updateById(questionSubmitupdate);
        if (!updateById) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitService.getById(questionSubmitId);
    }

}
