package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionExaminationPointService;

@Transactional(readOnly = true)
@Service
public class ResconQuestionExaminationPointServiceImpl implements ResconQuestionExaminationPointService {

	@Autowired
	@Qualifier("QuestionExaminationPointRepo")
	private Repo<QuestionExaminationPoint, Long> repo;
	@Autowired
	@Qualifier("ExaminationPointRepo")
	private Repo<ExaminationPoint, Long> examinationPointRepo;

	@Override
	public List<Long> findQuestionIds(List<Long> examIds, Long vendorId) {
		return repo.find("$findQuestionIds", Params.param("examIds", examIds).put("vendorId", vendorId)).list(
				Long.class);
	}

	@Override
	public List<ExaminationPoint> listByQuestion(Long questionId) {
		return repo.find("$findExaminationPointByQuestion", Params.param("questionId", questionId)).list(
				ExaminationPoint.class);
	}

	@Override
	public Map<Long, List<ExaminationPoint>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionExaminationPoint> qes = repo.find("$listByQuestions", Params.param("questionIds", questionIds))
				.list();
		Set<Long> examinationPointCodes = new HashSet<Long>(qes.size());
		for (QuestionExaminationPoint questionExaminationPoint : qes) {
			examinationPointCodes.add(questionExaminationPoint.getExaminationPointCode());
		}
		if (examinationPointCodes.size() > 0) {
			Map<Long, List<ExaminationPoint>> rmap = new HashMap<Long, List<ExaminationPoint>>(questionIds.size());
			Map<Long, ExaminationPoint> examinationPointMap = examinationPointRepo.mget(examinationPointCodes);
			for (QuestionExaminationPoint questionExaminationPoint : qes) {
				long questionId = questionExaminationPoint.getQuestionId();
				long examinationPointCode = questionExaminationPoint.getExaminationPointCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<ExaminationPoint>());
				}
				if (null != examinationPointMap.get(examinationPointCode)) {
					rmap.get(questionId).add(examinationPointMap.get(examinationPointCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	@Override
	public Page<Long> queryQuestionByExaminationPointCode(long examinationPointCode, Pageable pageable) {
		return repo.find("$findAllQuestionByExaminationPoint", Params.param("code", examinationPointCode)).fetch(
				pageable, Long.class);
	}
}
