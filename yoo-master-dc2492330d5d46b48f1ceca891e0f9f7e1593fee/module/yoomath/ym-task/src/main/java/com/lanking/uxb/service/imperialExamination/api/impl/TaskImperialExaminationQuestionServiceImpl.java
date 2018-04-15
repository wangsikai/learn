package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationQuestion;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationQuestionService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationQuestionServiceImpl implements TaskImperialExaminationQuestionService {
	

	@Autowired
	@Qualifier("ImperialExaminationQuestionRepo")
	private Repo<ImperialExaminationQuestion, Long> repo;

	@Override
	public List<ImperialExaminationQuestion> get(long code, ImperialExaminationType type, 
			 Integer room, Integer category,ImperialExaminationExamTag tag) {
		Params params = Params.param("code", code);
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
			params.put("tag", tag.getValue());
		}
		return repo.find("$TaskQueryActivityQuestion", params).list();
	}
}
