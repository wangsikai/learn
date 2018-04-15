package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityRankServiceImpl implements TaskImperialExaminationActivityRankService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRankRepo")
	private Repo<ImperialExaminationActivityRank, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationActivityRank rank) {
		repo.save(rank);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryTeacherScoreByClazzId(long activityCode,Integer room,ImperialExaminationType type) {
		Params params = Params.param("code", activityCode);
		if (type != null) {
			params.put("type", type.getValue());
		}
		if (room != null) {
			params.put("room", room);
		}
		return repo.find("$TaskqueryTeacherScores", params).list(Map.class);
	}

	@Override
	public void save(Collection<ImperialExaminationActivityRank> ranks) {
		repo.save(ranks);
	}

	@Override
	public List<ImperialExaminationActivityRank> queryAllTeacherRanks(long activityCode, ImperialExaminationType type,
			                              Integer tag,Integer room) {
		Params params = Params.param("code", activityCode);
		if (type != null) {
			params.put("type", type.getValue());
		}
		
		if (type != null) {
			params.put("tag", tag);
		}
		
		if (type != null) {
			params.put("room", room);
		}
		return repo.find("$TaskqueryTeacherAllScores", params).list();
	}

}
