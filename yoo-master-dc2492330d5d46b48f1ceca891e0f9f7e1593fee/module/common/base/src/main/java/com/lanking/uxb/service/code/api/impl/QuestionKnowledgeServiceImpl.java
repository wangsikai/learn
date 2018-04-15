package com.lanking.uxb.service.code.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class QuestionKnowledgeServiceImpl implements QuestionKnowledgeService {

	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	private Repo<QuestionKnowledge, Long> repo;

	@Override
	public List<Long> queryKnowledgeByQuestionId(Long questionId) {
		return repo.find("$queryKnowledgeByQuestionId", Params.param("questionId", questionId)).list(Long.class);
	}

	@Override
	public void removeKnowledgeCacheByQuestionId(Long questionId) {
		return;
	}

	@Override
	public List<Long> queryParentKnowledgeCodes(Collection<Long> codes) {
		Set<Long> set = new HashSet<Long>();
		List<Long> list = new ArrayList<Long>();
		for (Long code : codes) {
			set.addAll(this.findAllParentsCodeByKPoint(code));
		}
		list.addAll(set);
		return list;
	}

	@Override
	public Map<Long, Long> queryKpQuestions(int subjectCode) {
		List<Map> result = repo.find("$queryKnowledgeQuestionCount", Params.param("subjectCode", subjectCode))
				.list(Map.class);

		Map<Long, Long> retMap = new HashMap<Long, Long>(result.size());
		for (Map m : result) {
			Long code = ((BigInteger) m.get("knowledge_code")).longValue();
			Long count = ((BigInteger) m.get("c")).longValue();

			retMap.put(code, count);
		}

		return retMap;
	}

	@Override
	public List<QuestionKnowledge> findByQuestions(Collection<Long> questionIds) {
		return repo.find("$mgetByQuestions", Params.param("questionIds", questionIds)).list();
	}

	/**
	 * 知识点查询所有父级
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> findAllParentsCodeByKPoint(Long code) {
		Long third = code / 100;
		Long second = code / 1000;
		Long first = code / 100000;

		List<Long> codes = new ArrayList<Long>();
		codes.add(first);
		codes.add(second);
		codes.add(third);
		return codes;
	}

	@Override
	public Map<Long, List<Long>> mgetByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledge> qks = repo.find("$mgetByQuestions", Params.param("questionIds", questionIds)).list();
		if (qks.size() > 0) {
			Map<Long, List<Long>> rmap = new HashMap<Long, List<Long>>(questionIds.size());
			for (QuestionKnowledge questionKnowledge : qks) {
				long questionId = questionKnowledge.getQuestionId();
				long knowledgePointCode = questionKnowledge.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<Long>());
				}
				rmap.get(questionId).add(knowledgePointCode);
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

}
