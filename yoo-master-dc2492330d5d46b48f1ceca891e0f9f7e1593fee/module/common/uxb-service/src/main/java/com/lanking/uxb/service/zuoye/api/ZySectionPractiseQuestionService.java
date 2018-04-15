package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractiseQuestion;
import com.lanking.uxb.service.zuoye.form.SectionPractiseQuestionForm;

import java.util.List;

/**
 * 章节练习题目处理Service
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
public interface ZySectionPractiseQuestionService {
	/**
	 * 根据章节的id得到此练习下所有的题目列表
	 *
	 * @param practiseId
	 *            章节练习的id
	 * @return {@link SectionPractiseQuestion}
	 */
	List<SectionPractiseQuestion> mgetListByPractise(long practiseId);

	/**
	 * 暂存学生答题信息
	 *
	 * @param forms
	 *            {@link SectionPractiseQuestionForm}
	 * @param practiseId
	 *            章节练习id
	 */
	void draft(List<SectionPractiseQuestionForm> forms, long practiseId);

	/**
	 * 保存章节练习题目
	 *
	 * @param forms
	 *            {@link SectionPractiseQuestionForm}
	 */
	void commit(List<SectionPractiseQuestionForm> forms, long practiseId);
}
