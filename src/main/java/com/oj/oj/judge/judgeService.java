package com.oj.oj.judge;

import com.oj.oj.model.entity.QuestionSubmit;

/**
 * 判题服务
 * 
 * 
 */
public interface judgeService {
    /**
     * 判题
     * 
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudeg(long questionSubmitId);
}
