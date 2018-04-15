package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;

import com.lanking.cloud.domain.common.resource.question.QuestionSection;

/**
 * 习题章节接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年9月10日
 */
public interface ResconQuestionSectionService {

	/**
	 * 保存习题章节.
	 * 
	 * @param questionId
	 * @param questionSections
	 */
	void saveQuestionSections(long questionId, Collection<QuestionSection> questionSections);

	/**
	 * 保存习题章节.
	 * 
	 * @param questionSections
	 */
	void saveQuestionSections(Collection<QuestionSection> questionSections);
}
