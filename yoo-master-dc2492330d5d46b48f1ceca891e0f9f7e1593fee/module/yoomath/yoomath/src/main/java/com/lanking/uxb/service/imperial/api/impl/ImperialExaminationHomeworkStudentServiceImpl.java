package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkStudentService;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationHomeworkStudentServiceImpl implements ImperialExaminationHomeworkStudentService {

	@Autowired
	@Qualifier("ImperialExaminationHomeworkStudentRepo")
	private Repo<ImperialExaminationHomeworkStudent, Long> repo;

	@Override
	@Transactional
	public void save(ImperialExaminationHomeworkStudent entity) {
		repo.save(entity);
	}

	@Override
	@Transactional
	public void save(List<ImperialExaminationHomeworkStudent> entitys) {
		repo.save(entitys);
	}

	@Override
	public List<ImperialExaminationHomeworkStudent> list(Long code, ImperialExaminationType type, Long userId,
			Integer tag, Integer room) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		if (tag != null) {
			params.put("tag", tag);
		}
		if (room != null) {
			params.put("room", room);
		}

		return repo.find("$list", params).list();
	}

	@Override
	public ImperialExaminationHomeworkStudent get(long id) {
		return repo.get(id);
	}

}
