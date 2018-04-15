package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

/**
 * 批改题目接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月30日
 */
public interface ZyCorrectingService {

	/**
	 * 单选多选的批改
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param qIds
	 *            题目ID
	 * @param answerList
	 *            答案集合
	 * @return 批改结果（key:result,done;result对错done是否答题）
	 */
	List<Map<String, Object>> simpleCorrect(List<Long> qIds, List<Map<Long, List<String>>> answerList);

}
