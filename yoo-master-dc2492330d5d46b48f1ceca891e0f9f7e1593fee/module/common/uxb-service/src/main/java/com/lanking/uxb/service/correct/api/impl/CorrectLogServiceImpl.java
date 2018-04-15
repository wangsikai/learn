package com.lanking.uxb.service.correct.api.impl;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.correct.api.CorrectLogService;

/**
 * 批改日志接口实现.
 * 
 * @author wanlong.che
 *
 */
@Service
public class CorrectLogServiceImpl implements CorrectLogService {

	@Autowired
	@Qualifier("QuestionCorrectLogRepo")
	Repo<QuestionCorrectLog, Long> questionCorrectLogRepo;

	@Override
	@Transactional
	public void create(QuestionCorrectLog log) {
		questionCorrectLogRepo.save(log);
	}

	@Override
	@Transactional
	public void create(Collection<QuestionCorrectLog> logs) {
		questionCorrectLogRepo.save(logs);
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionCorrectLog get(long id) {
		return questionCorrectLogRepo.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionCorrectLog getNewestLog(long stuHkQId) {
		
		Params param = Params.param("stuHkQId",stuHkQId);
		
		return questionCorrectLogRepo.find("$findNewestLog", param).get(QuestionCorrectLog.class);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasCorrectd(long stuHkQId, QuestionCorrectType type, Date time) {
		Params param = Params.param("stuHkQId",stuHkQId);
		param.put("type", type.getValue());
		param.put("time", time);
		
		Integer count = questionCorrectLogRepo.find("$getCorrectLogCount", param).get(Integer.class);
		
		return count > 0;
	}
}
