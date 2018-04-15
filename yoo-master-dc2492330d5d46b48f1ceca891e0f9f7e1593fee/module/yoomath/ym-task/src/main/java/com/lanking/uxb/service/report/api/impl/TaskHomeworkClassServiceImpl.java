package com.lanking.uxb.service.report.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.TaskHomeworkClassService;

@Transactional(readOnly = true)
@Service
public class TaskHomeworkClassServiceImpl implements TaskHomeworkClassService {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Override
	public List<HomeworkClazz> listCurrentClazzs(long teacherId) {
		return homeworkClazzRepo.find("$taskZyQuery",
				Params.param("teacherId", teacherId).put("status", Status.ENABLED.getValue())).list();
	}

	@Override
	public HomeworkClazz get(long id) {
		return homeworkClazzRepo.get(id);
	}

	@Override
	public Map<Long, HomeworkClazz> mget(Collection<Long> ids) {
		return homeworkClazzRepo.mget(ids);
	}

	@Override
	public CursorPage<Long, HomeworkClazz> getAll(CursorPageable<Long> cursorPageable) {
		return homeworkClazzRepo.find("$taskGetAllByPage").fetch(cursorPageable);
	}

}
