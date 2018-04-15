package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStudentService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityRankStudentServiceImpl implements TaskImperialExaminationActivityRankStudentService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRankStudentRepo")
	private Repo<ImperialExaminationActivityRankStudent, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationActivityRankStudent rank) {
		repo.save(rank);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryStudentScoreByClazzId(long activityCode,ImperialExaminationType type) {
		Params params = Params.param("code", activityCode);
		if (type != null) {
			params.put("type", type.getValue());
		}
		return repo.find("$TaskqueryStudentScores", params).list(Map.class);
	}

	@Override
	public void save(Collection<ImperialExaminationActivityRankStudent> ranks) {
		repo.save(ranks);
	}

	@Override
	public List<ImperialExaminationActivityRankStudent> queryAllStudentRanks(long activityCode,
			ImperialExaminationType type,Integer tag) {
		Params params = Params.param("code", activityCode);
		if (type != null) {
			params.put("type", type.getValue());
		}
		
		if (tag != null) {
			params.put("tag", tag);
		}

		return repo.find("$TaskqueryStudentAllScores", params).list();
	}

}
