package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkStudentService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationHomeworkStudentServiceImpl implements TaskImperialExaminationHomeworkStudentService {

	@Autowired
	@Qualifier("ImperialExaminationHomeworkStudentRepo")
	private Repo<ImperialExaminationHomeworkStudent, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationHomeworkStudent homework) {
		repo.save(homework);
	}

	@Override
	public List<ImperialExaminationHomeworkStudent> query(long activityCode, ImperialExaminationType type,
			                                     Integer room, Integer category, Integer tag) {
		Params params = Params.param("code", activityCode);
		if (type != null) {
			params.put("type", type.getValue());
		}
		
		if (room != null) {
			params.put("room", room);
		}
		
		if (category != null) {
			params.put("category", category);
		}
		
		if (tag != null) {
			params.put("tag", tag);
		}
		
		return repo.find("$TaskQueryActivityHomeworkStudent", params).list();
	}

	@Override
	public List<ImperialExaminationHomeworkStudent> query(long activityCode) {
		return query(activityCode, null, null, null, null);
	}

	@Transactional
	@Override
	public void save(Collection<ImperialExaminationHomeworkStudent> homeworks) {
		repo.save(homeworks);
	}
}
