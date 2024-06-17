package com.oj.oj.judge.strategy;

import lombok.Data;

import java.util.List;

import com.oj.oj.model.dto.question.JudgeCase;
import com.oj.oj.model.dto.questionsubmit.JudgeInfo;
import com.oj.oj.model.entity.Question;
import com.oj.oj.model.entity.QuestionSubmit;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
