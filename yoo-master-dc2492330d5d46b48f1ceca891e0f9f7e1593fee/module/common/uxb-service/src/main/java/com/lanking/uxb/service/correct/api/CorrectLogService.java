package com.lanking.uxb.service.correct.api;

import java.util.Collection;
import java.util.Date;

import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;

/**
 * 批改日志接口.
 * 
 * @author wanlong.che
 *
 */
public interface CorrectLogService {

	/**
	 * 保存批改Log.
	 * 
	 * @param log
	 */
	void create(QuestionCorrectLog log);

	/**
	 * 保存批改Log.
	 * 
	 * @param log
	 */
	void create(Collection<QuestionCorrectLog> logs);

	/**
	 * 获取Log.
	 * 
	 * @param id
	 * @return
	 */
	QuestionCorrectLog get(long id);
	
	
	/**
	 * 获取某个问题的最近的一次批改Log.
	 * 
	 * @param id
	 * @return
	 */
	QuestionCorrectLog getNewestLog(long stuHkQId);
	
	/**
	 * 查询某个问题在一个时间以后是否有经过某种类型的批改
	 * 
	 * @param id
	 * @return
	 */
	boolean hasCorrectd(long stuHkQId, QuestionCorrectType type, Date time);
}
