package com.lanking.uxb.service.correct.api;

import java.util.Collection;

/**
 * 批改触发的统计相关数据处理接口.
 * 
 * @author wanlong.che
 *
 */
public interface CorrectStatService {

	/**
	 * 错题处理（异步）.
	 *
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题ID集合
	 */
	void aysncFallibleQuestionHandler(Collection<Long> studentHomeworkQuestionIds);
}
