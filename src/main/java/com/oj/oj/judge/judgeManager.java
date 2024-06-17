package com.oj.oj.judge;

import org.springframework.stereotype.Service;

import com.oj.oj.judge.strategy.DefaultJudgeStrategy;
import com.oj.oj.judge.strategy.JavaJudgeStrategy;
import com.oj.oj.judge.strategy.JudgeContext;
import com.oj.oj.judge.strategy.JudgeStrategy;
import com.oj.oj.model.dto.questionsubmit.JudgeInfo;
import com.oj.oj.model.entity.QuestionSubmit;

/**
 * 判题管理（简化调用）
 */
@Service
public class judgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
