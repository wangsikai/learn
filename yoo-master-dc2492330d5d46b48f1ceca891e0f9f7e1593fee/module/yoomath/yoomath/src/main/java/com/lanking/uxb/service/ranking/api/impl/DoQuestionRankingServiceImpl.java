package com.lanking.uxb.service.ranking.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingQuery;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingService;

@Transactional(readOnly = true)
@Service
public class DoQuestionRankingServiceImpl implements DoQuestionRankingService {

	@Autowired
	@Qualifier("DoQuestionClassStatRepo")
	private Repo<DoQuestionClassStat, Long> doQuestionClassStatRepo;
	@Autowired
	@Qualifier("DoQuestionSchoolStatRepo")
	private Repo<DoQuestionSchoolStat, Long> doQuestionSchoolStatRepo;

	@Override
	public List<DoQuestionClassStat> listDoQuestionClassStatTopN(DoQuestionRankingQuery query, int topN) {
		return doQuestionClassStatRepo.find("$zyListTopN",
				Params.param("classId", query.getClassId()).put("day0", query.getDay()).put("topN", topN)).list();
	}

	@Override
	public List<DoQuestionSchoolStat> listDoQuestionSchoolStatTopN(DoQuestionRankingQuery query, int topN) {
		return doQuestionSchoolStatRepo.find(
				"$zyListTopN",
				Params.param("schoolId", query.getSchoolId()).put("day0", query.getDay())
						.put("phaseCode", query.getPhaseCode()).put("topN", topN)).list();
	}

	@Override
	public DoQuestionClassStat findStudentInClassStat(DoQuestionRankingQuery query) {
		return doQuestionClassStatRepo.find(
				"$zyFindStudentInClassStat",
				Params.param("day0", query.getDay()).put("classId", query.getClassId())
						.put("userId", query.getStudentId())).get();
	}

	@Override
	public Page<DoQuestionSchoolStat> query(DoQuestionRankingQuery query, Pageable p) {
		return doQuestionSchoolStatRepo.find(
				"$queryQuesBySchool",
				Params.param("schoolId", query.getSchoolId()).put("day0", query.getDay())
						.put("phaseCode", query.getPhaseCode())).fetch(p);
	}

	@Override
	public Map<Long, DoQuestionClassStat> mgetDoQuestionClassStat(List<Long> classIds, Long studentId, int day) {
		List<DoQuestionClassStat> list = doQuestionClassStatRepo.find("$mgetDoQuestionClassStat",
				Params.param("day0", day).put("classIds", classIds).put("userId", studentId)).list();
		Map<Long, DoQuestionClassStat> map = new HashMap<Long, DoQuestionClassStat>();
		for (DoQuestionClassStat d : list) {
			map.put(d.getClassId(), d);
		}
		return map;
	}
}
