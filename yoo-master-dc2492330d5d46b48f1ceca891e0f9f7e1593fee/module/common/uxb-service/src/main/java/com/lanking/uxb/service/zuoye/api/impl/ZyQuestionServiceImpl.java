package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyQuestionService;

@Transactional(readOnly = true)
@Service
public class ZyQuestionServiceImpl implements ZyQuestionService {
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public List<Question> pullQuestions(Long sectionCode, Set<Type> types, int count, List<Long> exQIds,
			BigDecimal minDifficulty, BigDecimal maxDifficulty) {
		Params params = Params.param("count", count);
		if (sectionCode != null) {
			params.put("sectionCode", sectionCode);
		}
		if (CollectionUtils.isNotEmpty(types)) {
			Set<Integer> typeVals = new HashSet<Integer>(types.size());
			for (Type type : types) {
				typeVals.add(type.getValue());
			}
			params.put("types", typeVals);
		}
		if (CollectionUtils.isNotEmpty(exQIds)) {
			params.put("exQIds", exQIds);
		}
		if (minDifficulty != null) {
			params.put("minDifficulty", minDifficulty);
		}
		if (maxDifficulty != null) {
			params.put("maxDifficulty", maxDifficulty);
		}
		return questionRepo.find("$zyPullQuestions", params).list();
	}

	@Override
	public boolean hasQuestionAnswering(Collection<Long> ids) {
		return questionRepo.find("$hasQuestionAnswering", Params.param("ids", ids)).count() > 0;
	}

	@Override
	public List<Question> findByCodes(List<String> codes) {
		return questionRepo.find("$findByCodes", Params.param("codes", codes)).list();
	}
}
