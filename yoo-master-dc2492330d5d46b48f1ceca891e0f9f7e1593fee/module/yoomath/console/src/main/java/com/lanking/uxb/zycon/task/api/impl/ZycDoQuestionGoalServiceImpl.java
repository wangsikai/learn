package com.lanking.uxb.zycon.task.api.impl;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.uxb.zycon.task.api.ZycDoQuestionGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Service
@Transactional(readOnly = true)
public class ZycDoQuestionGoalServiceImpl implements ZycDoQuestionGoalService {
	@Autowired
	@Qualifier(value = "DoQuestionGoalRepo")
	private Repo<DoQuestionGoal, Long> repo;

	@Override
	public DoQuestionGoal findByUserId(long userId) {
		return repo.get(userId);
	}
}
