package com.lanking.uxb.operation.questionSection.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.operation.questionSection.api.OpQuestionSectionService;

@Service
@Transactional(readOnly = true)
public class OpQuestionSectionServiceImpl implements OpQuestionSectionService {

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, Long> repo;

	@Override
	@Transactional
	public void convert(int textbookCode, List<Long> questionIds) {
		repo.execute("$opDelete", Params.param("textbookCode", textbookCode).put("questionIds", questionIds));
		for (Long questionId : questionIds) {
			Params params = Params.param("textbookCode", textbookCode).put("questionId", questionId);
			List<Long> sectionCodes1 = repo.find("$opFindQuestionSection1", params).list(Long.class);
			List<Long> sectionCodes2 = repo.find("$opFindQuestionSection2", params).list(Long.class);
			for (Long sectionCode : sectionCodes1) {
				QuestionSection qs = new QuestionSection();
				qs.setQuestionId(questionId);
				qs.setSectionCode(sectionCode);
				qs.setTextBookCode(textbookCode);
				qs.setV1(true);
				qs.setV2(sectionCodes2.contains(sectionCode));
				repo.save(qs);
			}
			for (Long sectionCode : sectionCodes2) {
				if (!sectionCodes1.contains(sectionCode)) {
					QuestionSection qs = new QuestionSection();
					qs.setQuestionId(questionId);
					qs.setSectionCode(sectionCode);
					qs.setTextBookCode(textbookCode);
					qs.setV2(true);
					repo.save(qs);
				}
			}
		}
	}

}
