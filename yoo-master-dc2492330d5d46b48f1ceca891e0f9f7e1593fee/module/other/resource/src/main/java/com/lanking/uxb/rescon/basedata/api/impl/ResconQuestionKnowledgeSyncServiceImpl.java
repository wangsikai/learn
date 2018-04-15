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
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledgeSync;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeSyncService;

@Transactional(readOnly = true)
@Service
public class ResconQuestionKnowledgeSyncServiceImpl implements ResconQuestionKnowledgeSyncService {

	@Autowired
	@Qualifier("QuestionKnowledgeSyncRepo")
	private Repo<QuestionKnowledgeSync, Long> repo;

	@Autowired
	@Qualifier("KnowledgeSyncRepo")
	private Repo<KnowledgeSync, Long> ksRepo;

	@Override
	public List<KnowledgeSync> listByQuestion(Long questionId) {
		return repo.find("$resListKnowledgeSyncByQuestion", Params.param("questionId", questionId))
				.list(KnowledgeSync.class);
	}

	@Override
	public Map<Long, List<KnowledgeSync>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledgeSync> qkss = repo
				.find("$resListKnowledgeSyncByQuestions", Params.param("questionIds", questionIds)).list();
		Set<Long> knowledgeSyncCodes = new HashSet<Long>(qkss.size());
		for (QuestionKnowledgeSync questionKnowledgeSync : qkss) {
			knowledgeSyncCodes.add(questionKnowledgeSync.getKnowledgeCode());
		}
		if (knowledgeSyncCodes.size() > 0) {
			Map<Long, List<KnowledgeSync>> rmap = new HashMap<Long, List<KnowledgeSync>>(questionIds.size());
			Map<Long, KnowledgeSync> knowledgeSyncMap = ksRepo.mget(knowledgeSyncCodes);
			for (QuestionKnowledgeSync questionKnowledgeSync : qkss) {
				long questionId = questionKnowledgeSync.getQuestionId();
				long knowledgeSyncCode = questionKnowledgeSync.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<KnowledgeSync>());
				}
				if (null != knowledgeSyncMap.get(knowledgeSyncCode)) {
					rmap.get(questionId).add(knowledgeSyncMap.get(knowledgeSyncCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	@Override
	@Transactional
	public void saveQuestionKnowledgeSync(long questionId, List<Long> knowledgeSyncIds) {
		repo.execute("delete from question_knowledge_review where question_id=:questionId",
				Params.param("questionId", questionId));
		repo.execute("delete from question_knowledge_sync where question_id=:questionId",
				Params.param("questionId", questionId));
		if (CollectionUtils.isEmpty(knowledgeSyncIds)) {
			return;
		}
		for (int i = 0; i < knowledgeSyncIds.size(); i++) {
			QuestionKnowledgeSync questionKnowledgeSync = new QuestionKnowledgeSync();
			questionKnowledgeSync.setQuestionId(questionId);
			questionKnowledgeSync.setKnowledgeCode(knowledgeSyncIds.get(i));
			repo.save(questionKnowledgeSync);
		}

	}
}
