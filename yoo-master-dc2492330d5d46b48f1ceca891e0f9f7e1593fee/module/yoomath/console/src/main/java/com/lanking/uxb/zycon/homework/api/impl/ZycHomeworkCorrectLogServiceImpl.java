package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLog;
import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLogType;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkCorrectLogService;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectLogForm;

/**
 * @author xinyu.zhou
 * @see ZycHomeworkCorrectLogService
 * @since yoomath V1.7
 */
@SuppressWarnings("Duplicates")
@Service
@Transactional(readOnly = true)
public class ZycHomeworkCorrectLogServiceImpl implements ZycHomeworkCorrectLogService {

	@Autowired
	@Qualifier("HomeworkCorrectLogRepo")
	private Repo<HomeworkCorrectLog, Long> repo;

	@Override
	@Transactional
	public HomeworkCorrectLog save(long userId, HomeworkCorrectLogForm form, HomeworkCorrectLogType type) {
		HomeworkCorrectLog log = new HomeworkCorrectLog();
		log.setUserId(userId);
		log.setStudentHomeworkQuestionId(form.getSqId());
		log.setCreateAt(new Date());
		log.setResult(form.getResult());
		log.setType(type);

		return repo.save(log);
	}

	@Async
	@Override
	@Transactional
	public void save(long userId, Collection<HomeworkCorrectLogForm> forms, HomeworkCorrectLogType type) {
		for (HomeworkCorrectLogForm form : forms) {
			HomeworkCorrectLog log = new HomeworkCorrectLog();
			log.setUserId(userId);
			log.setStudentHomeworkQuestionId(form.getSqId());
			log.setResult(form.getResult());
			log.setCreateAt(new Date());
			log.setType(type);

			repo.save(log);
		}
	}
}
