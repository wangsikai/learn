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
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeService;

@Transactional(readOnly = true)
@Service
public class ResconQuestionKnowledgeServiceImpl implements ResconQuestionKnowledgeService {

	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	private Repo<QuestionKnowledge, Long> repo;
	@Autowired
	@Qualifier("KnowledgePointRepo")
	private Repo<KnowledgePoint, Long> knowledgePointRepo;

	@Override
	public List<Long> findQuestionIds(List<Long> knowpointCodes, Long vendorId) {
		return repo.find("$findQuestionIds", Params.param("knowpointCodes", knowpointCodes).put("vendorId", vendorId))
				.list(Long.class);
	}

	@Override
	public List<KnowledgePoint> listByQuestion(Long questionId) {
		return repo.find("$findKnowledgePointByQuestion", Params.param("questionId", questionId)).list(
				KnowledgePoint.class);
	}

	@Override
	public Map<Long, List<KnowledgePoint>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledge> qks = repo.find("$listByQuestions", Params.param("questionIds", questionIds)).list();
		Set<Long> knowledgePointCodes = new HashSet<Long>(qks.size());
		for (QuestionKnowledge questionKnowledge : qks) {
			knowledgePointCodes.add(questionKnowledge.getKnowledgeCode());
		}
		if (knowledgePointCodes.size() > 0) {
			Map<Long, List<KnowledgePoint>> rmap = new HashMap<Long, List<KnowledgePoint>>(questionIds.size());
			Map<Long, KnowledgePoint> knowledgePointMap = knowledgePointRepo.mget(knowledgePointCodes);
			for (QuestionKnowledge questionKnowledge : qks) {
				long questionId = questionKnowledge.getQuestionId();
				long knowledgePointCode = questionKnowledge.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<KnowledgePoint>());
				}
				if (null != knowledgePointMap.get(knowledgePointCode)) {
					rmap.get(questionId).add(knowledgePointMap.get(knowledgePointCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}
}
