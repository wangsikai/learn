package com.lanking.uxb.zycon.homework.api;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
public interface ZycHomeworkQuestionService {
	/**
	 * 根据Homework -> id得到作业中包含的题目id
	 *
	 * @param hkId
	 *            Homework id
	 * @return 题目的id集合
	 */
	List<Long> getQuestion(long hkId);
}
