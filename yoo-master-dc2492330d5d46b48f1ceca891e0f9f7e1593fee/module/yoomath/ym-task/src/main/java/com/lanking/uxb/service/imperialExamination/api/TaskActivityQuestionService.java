package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 题目相关接口
 * 
 * @since 2.1
 * @author zemin.song
 * @version 2016年4月11日
 */
public interface TaskActivityQuestionService {

	List<Question> mgetList(Collection<Long> ids);

	List<Question> getSubQuestions(long id);

	Map<Long, Question> mget(Collection<Long> ids);

	/**
	 * 是否有简答题
	 * 
	 * @since yoomath V1.9.1
	 * @param ids
	 *            题目ID
	 * @return 是否有简答题
	 */
	boolean hasQuestionAnswering(Collection<Long> ids);

}
