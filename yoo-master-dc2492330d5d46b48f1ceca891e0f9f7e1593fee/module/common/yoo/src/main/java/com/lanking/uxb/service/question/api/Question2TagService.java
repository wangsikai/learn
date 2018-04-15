package com.lanking.uxb.service.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question2Tag;

/**
 * 题目对应的标签
 * 
 * @since 1.3.0(教师端)
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月24日
 */
public interface Question2TagService {

	Question2Tag get(long id);

	Map<Long, Question2Tag> mget(Collection<Long> ids);

	/**
	 * 通过题目id查询对应标签id
	 * 
	 * @param questionId
	 */
	List<Question2Tag> getByQuestionId(long questionId);

	/**
	 * 通过题目id查询对应标签id
	 * 
	 * @param questionId
	 */
	Map<Long, List<Question2Tag>> mgetByQuestionIds(Collection<Long> questionIds);

}
