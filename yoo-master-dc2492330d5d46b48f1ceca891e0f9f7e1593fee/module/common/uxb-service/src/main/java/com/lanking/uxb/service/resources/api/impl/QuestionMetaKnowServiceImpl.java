package com.lanking.uxb.service.resources.api.impl;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.question.QuestionMetaKnow;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.resources.api.QuestionMetaKnowService;

@Transactional(readOnly = true)
@Service
public class QuestionMetaKnowServiceImpl implements QuestionMetaKnowService {
	@Autowired
	@Qualifier("QuestionMetaKnowRepo")
	Repo<QuestionMetaKnow, Integer> qmknowpointRepo;
	@Autowired
	@Qualifier("MetaKnowpointRepo")
	Repo<MetaKnowpoint, Integer> mknowpointRepo;

	@Override
	public List<MetaKnowpoint> listByQuestion(long questionId) {
		List<QuestionMetaKnow> qmks = qmknowpointRepo.find("$listByQuestion", Params.param("questionId", questionId))
				.list();
		Set<Integer> metaknows = new HashSet<Integer>(qmks.size());
		for (QuestionMetaKnow questionMetaKnow : qmks) {
			metaknows.add(questionMetaKnow.getMetaCode());
		}
		if (metaknows.size() > 0) {
			return Lists.newArrayList(mknowpointRepo.mget(metaknows).values());
		}

		return Lists.newArrayList();
	}

	@Override
	public Map<Long, List<MetaKnowpoint>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionMetaKnow> qmks = qmknowpointRepo.find("$listByQuestions", Params.param("questionIds", questionIds))
				.list();
		Set<Integer> metaknows = new HashSet<Integer>(qmks.size());
		for (QuestionMetaKnow questionMetaKnow : qmks) {
			metaknows.add(questionMetaKnow.getMetaCode());
		}
		if (metaknows.size() > 0) {
			Map<Long, List<MetaKnowpoint>> rmap = new HashMap<Long, List<MetaKnowpoint>>(questionIds.size());
			Map<Integer, MetaKnowpoint> metaKnowpointMap = mknowpointRepo.mget(metaknows);
			for (QuestionMetaKnow questionMetaKnow : qmks) {
				long questionId = questionMetaKnow.getQuestionId();
				int metaCode = questionMetaKnow.getMetaCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<MetaKnowpoint>());
				}
				if (null != metaKnowpointMap.get(metaCode)) {
					rmap.get(questionId).add(metaKnowpointMap.get(metaCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	@Override
	public Map<Integer, Integer> mGetQuestionCountsByNoNewKnowledge(Integer subjectCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
