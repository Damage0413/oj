package com.oj.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oj.oj.common.ErrorCode;
import com.oj.oj.constant.CommonConstant;
import com.oj.oj.exception.BusinessException;
import com.oj.oj.exception.ThrowUtils;
import com.oj.oj.mapper.QuestionMapper;
import com.oj.oj.model.dto.question.QuestionQueryRequest;
import com.oj.oj.model.entity.Question;
import com.oj.oj.model.entity.User;
import com.oj.oj.model.vo.QuestionVO;
import com.oj.oj.model.vo.UserVO;
import com.oj.oj.service.QuestionService;
import com.oj.oj.service.UserService;
import com.oj.oj.utils.SqlUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 题目服务实现
 *
 * 
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

        @Resource
        private UserService userService;

        @Override
        public void validQuestion(Question question, boolean add) {
                log.info("验证请求数据，{}", question);
                if (question == null) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR);
                }
                String title = question.getTitle();
                String content = question.getContent();
                String tags = question.getTags();
                String answer = question.getAnswer();
                String judgeCase = question.getJudgeCase();
                String judgeConfig = question.getJudgeConfig();
                // 创建时，参数不能为空
                if (add) {
                        ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
                }
                // 有参数则校验
                if (StringUtils.isNotBlank(title) && title.length() > 80) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
                }
                if (StringUtils.isNotBlank(content) && content.length() > 8192) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
                }
                if (StringUtils.isNotBlank(answer) && content.length() > 8192) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
                }
                if (StringUtils.isNotBlank(judgeCase) && content.length() > 8192) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
                }
                if (StringUtils.isNotBlank(judgeConfig) && content.length() > 8192) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
                }
        }

        /**
         * 获取查询包装类
         *
         * @param questionQueryRequest
         * @return
         */
        @Override
        public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
                log.info("获取查询包装类");
                QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
                if (questionQueryRequest == null) {
                        return queryWrapper;
                }
                Long id = questionQueryRequest.getId();
                String title = questionQueryRequest.getTitle();
                String content = questionQueryRequest.getContent();
                List<String> tags = questionQueryRequest.getTags();
                String answer = questionQueryRequest.getAnswer();
                Long userId = questionQueryRequest.getUserId();
                String sortField = questionQueryRequest.getSortField();
                String sortOrder = questionQueryRequest.getSortOrder();

                // 拼接查询条件
                queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
                queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
                queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
                if (CollUtil.isNotEmpty(tags)) {
                        for (String tag : tags) {
                                queryWrapper.like("tags", "\"" + tag + "\"");
                        }
                }
                queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
                queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
                queryWrapper.eq("isDelete", false);
                queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                                sortField);
                return queryWrapper;
        }

        /**
         * 封装信息，脱敏
         */
        @Override
        public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
                QuestionVO questionVO = QuestionVO.objToVo(question);
                // 1. 关联查询用户信息
                Long userId = question.getUserId();
                User user = null;
                if (userId != null && userId > 0) {
                        user = userService.getById(userId);
                }
                UserVO userVO = userService.getUserVO(user);
                questionVO.setUserVO(userVO);
                return questionVO;
        }

        /**
         * 获取封装后的查询信息
         */

        @Override
        public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
                List<Question> questionList = questionPage.getRecords();
                Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(),
                                questionPage.getTotal());
                if (CollUtil.isEmpty(questionList)) {
                        return questionVOPage;
                }
                // 1. 关联查询用户信息
                Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
                Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                                .collect(Collectors.groupingBy(User::getId));

                // 填充信息
                List<QuestionVO> questionVOList = questionList.stream().map(question -> {
                        QuestionVO questionVO = QuestionVO.objToVo(question);
                        Long userId = question.getUserId();
                        User user = null;
                        if (userIdUserListMap.containsKey(userId)) {
                                user = userIdUserListMap.get(userId).get(0);
                        }
                        questionVO.setUserVO(userService.getUserVO(user));
                        return questionVO;
                }).collect(Collectors.toList());
                questionVOPage.setRecords(questionVOList);
                return questionVOPage;
        }

}
