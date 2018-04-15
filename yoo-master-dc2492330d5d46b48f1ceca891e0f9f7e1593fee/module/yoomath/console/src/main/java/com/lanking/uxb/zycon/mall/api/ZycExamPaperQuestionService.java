package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;

/**
 * 中央资源库！试卷题目相关Service
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface ZycExamPaperQuestionService {
	/**
	 * 查询试卷里的题目
	 *
	 * @param examId
	 *            试卷id
	 * @return {@link List}
	 */
	List<ExamPaperQuestion> getExamQuestion(long examId);

	/**
	 * 取得多个试卷中的题目数量
	 *
	 * @param examIds
	 *            试卷id列表
	 * @return {@link Map}
	 */
	Map<Long, Integer> getExampaperQuestionCount(Collection<Long> examIds);
}
