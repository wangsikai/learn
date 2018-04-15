package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.uxb.service.code.api.PasswordQuestionService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class PasswordQuestionServiceImpl implements PasswordQuestionService {

	@Autowired
	@Qualifier("PasswordQuestionRepo")
	private Repo<PasswordQuestion, Integer> pqRepo;

	@Override
	public PasswordQuestion get(Integer code) {
		return pqRepo.get(code);
	}

	@Override
	public List<PasswordQuestion> getAll() {
		return pqRepo.getAll();
	}

	@Override
	public List<PasswordQuestion> mgetList(Collection<Integer> codes) {
		return pqRepo.mgetList(codes);
	}

	@Override
	public Map<Integer, PasswordQuestion> mget(Collection<Integer> codes) {
		return pqRepo.mget(codes);
	}

}
