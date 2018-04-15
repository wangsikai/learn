package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 提供习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:40
 */
public interface ZycQuestionService {

	/**
	 * 获取一个习题
	 * 
	 * @param id
	 *            习题ID
	 * @return 习题对象
	 */
	Question get(long id);

	/**
	 * 批量获取习题
	 * 
	 * @param id
	 *            习题IDs
	 * @return 习题对象
	 */
	Map<Long, Question> mget(Collection<Long> ids);

	/**
	 * 批量获取习题
	 * 
	 * @param id
	 *            习题IDs
	 * @return 习题对象
	 */
	List<Question> mgetList(Collection<Long> ids);

	/**
	 * 获取子题
	 * 
	 * @param id
	 *            父题ID
	 * @return 子题集合
	 */
	List<Question> getSubQuestions(long id);

	/**
	 * 根据 习题编码获取 题目 题目编码（过滤不存在的题目编码和没有标记通过的题目编码）
	 * 
	 * @param qCodes
	 * @return
	 */
	List<Question> findQuestionByCode(List<String> qCodes);

	List<Long> findQuestionIdsByCode(List<String> qCodes, String isOrderDif);

	/**
	 * 批量获取题目子题
	 * 
	 * @since yoomathV1.2
	 * @param ids
	 *            父题IDs
	 * @return Map
	 */
	Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids);

	/**
	 * 得到题目编号对应题目id的Map
	 *
	 * @param codes
	 *            编号列表
	 * @return Map
	 */
	Map<String, Question> mgetByCode(Collection<String> codes);

	/**
	 * @since yoomathV1.5
	 * @param id
	 *            题目ID
	 * @param schoolId
	 *            学校ID
	 */
	void updateSchool(long id, long schoolId);

	void updateSchool(Collection<Long> ids, long schoolId);
}
