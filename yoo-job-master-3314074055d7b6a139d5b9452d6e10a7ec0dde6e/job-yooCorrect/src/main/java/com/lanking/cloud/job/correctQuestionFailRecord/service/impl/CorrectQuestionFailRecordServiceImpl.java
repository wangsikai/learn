package com.lanking.cloud.job.correctQuestionFailRecord.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;
import com.lanking.cloud.job.correctQuestionFailRecord.DAO.CorrectQuestionFailRecordDao;
import com.lanking.cloud.job.correctQuestionFailRecord.service.CorrectQuestionFailRecordService;

/**
 * <p>Title:</p>
 * <p>Description:<p>
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
@Service
public class CorrectQuestionFailRecordServiceImpl implements CorrectQuestionFailRecordService {

	@Autowired
	CorrectQuestionFailRecordDao correctQuestionFailRecordDao;
	
	@Override
	@Transactional("transactionManager")
	public List<CorrectQuestionFailRecord> queryCorrectQuestionFailRecords() {
		return correctQuestionFailRecordDao.queryCorrectQuestionFailRecords();
	}

	@Override
	@Transactional("transactionManager")
	public void batchSave(Collection<Long> ids) {
		correctQuestionFailRecordDao.batchSave(ids);
	}

}
