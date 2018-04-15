package com.lanking.uxb.zycon.base.api;

import java.util.List;

/**
 * 练习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:23
 */
public interface ZycExerciseService {

	/**
	 * 通过教师ID和预置练习ID拉取题目列表
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @param exerciseId
	 *            习题ID如果为空则根据textbookExerciseId生成
	 * @return 习题ID列表
	 */
	List<Long> listQuestions(long teacherId, long textbookExerciseId);

}
