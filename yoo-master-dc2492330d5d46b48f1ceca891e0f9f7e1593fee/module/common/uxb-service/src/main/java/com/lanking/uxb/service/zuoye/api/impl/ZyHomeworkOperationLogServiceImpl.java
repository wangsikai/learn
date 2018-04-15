package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.HomeworkOperationLog;
import com.lanking.cloud.domain.yoomath.homework.HomeworkOperationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkOperationLogService;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkOperationLogServiceImpl implements ZyHomeworkOperationLogService {

	@Autowired
	@Qualifier("HomeworkOperationLogRepo")
	Repo<HomeworkOperationLog, Long> repo;

	@Transactional
	@Override
	public HomeworkOperationLog log(HomeworkOperationType type, long homeworkId) {
		if (exist(type, homeworkId)) {
			return null;
		}
		HomeworkOperationLog log = new HomeworkOperationLog();
		log.setCreateAt(new Date());
		log.setHomeworkId(homeworkId);
		log.setType(type);
		return repo.save(log);
	}

	@Override
	public boolean exist(HomeworkOperationType type, long homeworkId) {
		return repo.find("$zyExist", Params.param("type", type.getValue()).put("hkId", homeworkId)).count() > 0;
	}

}
