package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationHomeworkServiceImpl implements TaskImperialExaminationHomeworkService {

	@Autowired
	@Qualifier("ImperialExaminationHomeworkRepo")
	private Repo<ImperialExaminationHomework, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationHomework homework) {
		repo.save(homework);
	}

	@Override
	public List<ImperialExaminationHomework> query(long activityCode, ImperialExaminationType type,
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
		return repo.find("$TaskQueryActivityHomework", params).list();
	}

	@Override
	public List<ImperialExaminationHomework> query(long activityCode) {
		return query(activityCode, null,null,null,null);
	}
}
