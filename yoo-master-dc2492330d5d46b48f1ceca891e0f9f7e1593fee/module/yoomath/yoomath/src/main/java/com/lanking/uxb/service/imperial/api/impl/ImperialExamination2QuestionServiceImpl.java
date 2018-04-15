package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExamination2Question;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExamination2QuestionService;

@Service
@Transactional(readOnly = true)
public class ImperialExamination2QuestionServiceImpl implements ImperialExamination2QuestionService {

	@Autowired
	@Qualifier("ImperialExamination2QuestionRepo")
	private Repo<ImperialExamination2Question, Long> repo;

	@Override
	public List<ImperialExamination2Question> getQuestions(Long code, ImperialExaminationType type,
			ImperialExaminationGrade grade, Integer textbookCategoryCode, Integer tag) {
		Params params = Params.param();
		params.put("code", code);
		params.put("type", type.getValue());
		params.put("grade", grade.ordinal());
		params.put("textbookCategoryCode", textbookCategoryCode);
		params.put("tag", tag);

		return repo.find("$findQuestions", params).list();
	}

	@Override
	@Transactional
	public void save(ImperialExamination2Question question) {
		repo.save(question);
	}
}
