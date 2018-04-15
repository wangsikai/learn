package com.lanking.intercomm.yoocorrect.service;

import java.util.Collection;

import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;

/**
 * 小悠快批待传输习题存储.
 * 
 * @author wanlong.che
 *
 */
public interface CorrectQuestionFailRecordService {

	/**
	 * 存储待传输习题集合.
	 * 
	 * @param records
	 *            记录
	 */
	void save(Collection<CorrectQuestionFailRecord> records);
}
