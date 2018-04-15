package com.lanking.uxb.service.examPaper.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperForm;

/**
 * 组卷题目相关接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface CustomExampaperQuestionService {
	/**
	 * 根据组卷id获得其所有题目(没有排序！！！ 目前只是教师开卷时用，请使用的人注意)
	 *
	 * @param examPaperId
	 *            组卷id
	 * @return {@link List}
	 */
	List<CustomExampaperQuestion> findByPaper(long examPaperId);

	/**
	 * 组卷题目提交 (先清空组卷组卷相关，在重新添加)
	 * 
	 * @param form
	 */
	void updateCustomExamQuesions(CustomExamPaperForm form);
}
