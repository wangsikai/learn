package com.lanking.uxb.service.doQuestion.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalCount;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalLevel;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.doQuestion.api.DoQuestionGoalService;

@Transactional(readOnly = true)
@Service
public class DoQuestionGoalServiceImpl implements DoQuestionGoalService {

	@Autowired
	@Qualifier("DoQuestionGoalRepo")
	private Repo<DoQuestionGoal, Long> goalRepo;
	@Autowired
	@Qualifier("DoQuestionGoalCountRepo")
	private Repo<DoQuestionGoalCount, Long> goalCountRepo;

	@Override
	public DoQuestionGoal findByUserId(long userId) {
		return goalRepo.get(userId);
	}

	@Transactional(readOnly = false)
	@Override
	public void setDoQuestionGoal(long userId, DoQuestionGoalLevel level, int goal) {
		DoQuestionGoal doQuestionGoal = findByUserId(userId);
		if (doQuestionGoal == null) {
			doQuestionGoal = new DoQuestionGoal();
			doQuestionGoal.setUserId(userId);
		}
		doQuestionGoal.setLevel(level);
		doQuestionGoal.setGoal(goal);
		goalRepo.save(doQuestionGoal);
	}

	@Override
	public DoQuestionGoalCount findByUserId(long userId, long date0) {
		return goalCountRepo.find("$findByUserId", Params.param("userId", userId).put("date0", date0)).get();
	}

	@Transactional(readOnly = false)
	@Override
	public void incrDoQuestionGoalCount(long userId, long date0, int delta) {
		int ret = goalCountRepo.execute("$incrCount",
				Params.param("userId", userId).put("date0", date0).put("delta", delta));
		if (ret == 0) {
			DoQuestionGoalCount count = findByUserId(userId, date0);
			if (count == null) {
				count = new DoQuestionGoalCount();
				count.setDate0(date0);
				count.setGoal(delta);
				count.setUserId(userId);
				goalCountRepo.save(count);
			}
		}
	}
}
