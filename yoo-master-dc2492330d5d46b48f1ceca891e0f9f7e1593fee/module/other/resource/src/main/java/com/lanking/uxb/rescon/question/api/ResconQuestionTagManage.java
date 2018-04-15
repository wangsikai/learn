package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 题目标签管理接口.
 * 
 * @author wlche
 *
 */
public interface ResconQuestionTagManage {

	/**
	 * 批量获取标签.
	 * 
	 * @param codes
	 * @return
	 */
	Map<Long, QuestionTag> mget(Collection<Long> codes);

	/**
	 * 获取所有的标签.
	 * 
	 * @return
	 */
	List<QuestionTag> listAll(Status status, QuestionTagType questionTagType);

	/**
	 * 创建、更新标签.
	 * 
	 * @param code
	 * @param name
	 * @param shortName
	 * @param cfg
	 * @param icon
	 * @param squence
	 * @param status
	 * @return
	 */
	QuestionTag saveOrUpdateTag(Long code, String name, String shortName, String cfg, Long icon, Integer squence,
			Status status);

	/**
	 * 移动标签.
	 * 
	 * @param code
	 * @param op
	 * @return
	 */
	QuestionTag moveTag(Long code, String op);

	/**
	 * 获取题目对应的标签.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<Question2Tag> listByQuestion(long questionId);

	/**
	 * 批量获取题目对应的标签.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<Question2Tag>> mgetByQuestions(Collection<Long> questionIds);
}
