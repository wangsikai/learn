package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityUserService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityUserServiceImpl implements TaskImperialExaminationActivityUserService {

	@Autowired
	@Qualifier("ImperialExaminationActivityUserRepo")
	private Repo<ImperialExaminationActivityUser, Long> repo;

	@Override
	public List<ImperialExaminationActivityUser> get(long code, Integer room, Integer category) {
		Params params = Params.param("code", code);
		
		if (room != null) {
			params.put("room", room);
		}
		
		if (category != null) {
			params.put("category", category);
		}
		
		return repo.find("$TaskQueryActivityUser", params).list();
	}
}
