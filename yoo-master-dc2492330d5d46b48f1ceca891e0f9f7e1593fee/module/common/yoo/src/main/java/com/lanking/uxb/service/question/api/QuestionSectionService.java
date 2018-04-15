package com.lanking.uxb.service.question.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.QuestionSection;

/**
 * @since 2.2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月2日
 */
public interface QuestionSectionService {

	/**
	 * 获取题目对应的章节
	 * 
	 * @since 2.3.0
	 * @param version
	 *            版本
	 * @param questionId
	 *            题目ID
	 * 
	 * @return List
	 */
	List<QuestionSection> findByQuestionId(int version, long questionId);

	/**
	 * 统计章节对应题目的数量
	 * 
	 * @since 2.3.0
	 * @param version
	 *            版本
	 * @param textbookCode
	 *            教材CODE
	 * @param excludeQuestionTypeCodes
	 *            排除的题目类型代码集合
	 * @return Map
	 */
	Map<Long, Long> statisSectionQuestionCount(int version, Integer textbookCode, List<Integer> excludeQuestionTypeCodes);
}
