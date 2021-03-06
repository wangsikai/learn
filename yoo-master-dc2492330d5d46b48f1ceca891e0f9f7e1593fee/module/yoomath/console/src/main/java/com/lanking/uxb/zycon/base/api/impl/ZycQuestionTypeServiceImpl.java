package com.lanking.uxb.zycon.base.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.base.api.ZycQuestionTypeService;

@Transactional(readOnly = true)
@Service
public class ZycQuestionTypeServiceImpl implements ZycQuestionTypeService {

	@Autowired
	@Qualifier("QuestionTypeRepo")
	Repo<QuestionType, Integer> questionTypeRepo;

	@Override
	public QuestionType get(Integer code) {
		return questionTypeRepo.get(code);
	}

	@Override
	public Map<Integer, QuestionType> mget(Collection<Integer> codes) {
		return questionTypeRepo.mget(codes);
	}

	@Override
	public List<QuestionType> mgetList(Collection<Integer> codes) {
		return questionTypeRepo.mgetList(codes);
	}

	@Override
	public List<QuestionType> findBySubject(Integer subjectCode) {
		return questionTypeRepo.find("$findBySubject", Params.param("subjectCode", subjectCode)).list();
	}

}
