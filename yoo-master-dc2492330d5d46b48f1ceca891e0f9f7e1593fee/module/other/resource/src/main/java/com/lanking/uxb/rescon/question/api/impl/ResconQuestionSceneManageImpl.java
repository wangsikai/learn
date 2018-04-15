package com.lanking.uxb.rescon.question.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionScene;
import com.lanking.uxb.rescon.question.api.ResconQuestionSceneManage;

@Transactional(readOnly = true)
@Service
public class ResconQuestionSceneManageImpl implements ResconQuestionSceneManage {
	@Autowired
	@Qualifier("QuestionSceneRepo")
	Repo<QuestionScene, Integer> questionSceneRepo;

	@Override
	public QuestionScene get(int code) {
		return questionSceneRepo.get(code);
	}

	@Override
	public Map<Integer, QuestionScene> mget(Collection<Integer> codes) {
		return questionSceneRepo.mget(codes);
	}

	@Override
	public List<QuestionScene> mgetList(Collection<Integer> codes) {
		return questionSceneRepo.mgetList(codes);
	}

	@Override
	public List<QuestionScene> findList() {
		return questionSceneRepo.find("select * from question_scene order by code ASC").list();
	}
}
