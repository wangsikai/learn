package com.lanking.cloud.job.correctQuestionFailRecord.service;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;

/**
 * <p>
 * Description:传题记录Service
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优快批
 */
public interface CorrectQuestionFailRecordService {
	List<CorrectQuestionFailRecord> queryCorrectQuestionFailRecords();
	void batchSave(Collection<Long> ids);
}
