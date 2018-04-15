package com.lanking.uxb.service.index.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;

/**
 * 相似题使用.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月18日
 */
@Service
@Transactional(readOnly = true)
public class QuestionSimilarService {
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	Repo<QuestionKnowledge, Long> questionKnowledgeRepo;

	public Question getQuestion(long questionId) {
		return questionRepo.get(questionId);
	}

	public List<Question> mgetQuestionList(Collection<Long> questionIds) {
		return questionRepo.mgetList(questionIds);
	}

	public Page<Question> querySimilarBaseQuestion(int page, int pageSize) {
		return questionRepo.find("$indexSimilarQueryByPage", Params.param()).fetch(P.index(page, pageSize));
	}

	public Page<Question> queryNewSimilarBaseQuestion(int page, int pageSize) {
		return questionRepo.find("$indexSimilarNewQueryByPage", Params.param()).fetch(P.index(page, pageSize));
	}

	// 新知识点
	public List<Long> knowledgePointListByQuestion(Long questionId) {
		return questionKnowledgeRepo.find("$indexFindKnowledgePointByQuestion", Params.param("questionId", questionId))
				.list(Long.class);
	}

	public Map<Long, List<Long>> knowledgePointMListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledge> qks = questionKnowledgeRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
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
