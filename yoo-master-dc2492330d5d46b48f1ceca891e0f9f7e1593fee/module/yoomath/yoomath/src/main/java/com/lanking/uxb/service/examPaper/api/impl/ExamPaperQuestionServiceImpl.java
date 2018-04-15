package com.lanking.uxb.service.examPaper.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.api.ExamPaperQuestionService;

/**
 * @see ExamPaperQuestionService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class ExamPaperQuestionServiceImpl implements ExamPaperQuestionService {
	@Autowired
	@Qualifier("ExamPaperQuestionRepo")
	private Repo<ExamPaperQuestion, Long> repo;
	@Autowired
	@Qualifier("ExamPaperTopicRepo")
	private Repo<ExamPaperTopic, Long> topicRepo;

	@Override
	public List<ExamPaperQuestion> getExamQuestion(long examId) {
		List<ExamPaperTopic> topics = topicRepo.find("$zyFindByPaper", Params.param("paperId", examId)).list();
		List<ExamPaperQuestion> questions = repo.find("$zyGetExamQuestion", Params.param("examId", examId)).list();
		Map<Long, List<ExamPaperQuestion>> questionMap = new HashMap<Long, List<ExamPaperQuestion>>(questions.size());
		for (ExamPaperQuestion q : questions) {
			List<ExamPaperQuestion> list = questionMap.get(q.getTopicId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(q);
			questionMap.put(q.getTopicId(), list);
		}

		// 按照topic进行排序
		List<ExamPaperQuestion> retQuestions = new ArrayList<ExamPaperQuestion>(questions.size());
		for (ExamPaperTopic topic : topics) {
			if (questionMap.get(topic.getId()) != null) {
				retQuestions.addAll(questionMap.get(topic.getId()));
			}
		}

		return retQuestions;
	}

	@Override
	public Map<Long, Integer> getExampaperQuestionCount(Collection<Long> examIds) {
		List<Map> queryList = repo.find("$findExamQuestionCount", Params.param("examIds", examIds)).list(Map.class);
		Map<Long, Integer> retMap = new HashMap<Long, Integer>(queryList.size());
		for (Map m : queryList) {
			Long examPaperId = ((BigInteger) m.get("exam_paper_id")).longValue();
			Integer questionCount = ((BigInteger) m.get("c")).intValue();

			retMap.put(examPaperId, questionCount);
		}

		return retMap;
	}

	@Override
	public Map<Long, BigDecimal> getExampaperQuestionDifficulty(Collection<Long> examIds) {
		List<Map> queryList = repo.find("$findExamQuestionDifficulty", Params.param("examIds", examIds))
				.list(Map.class);
		Map<Long, BigDecimal> retMap = new HashMap<Long, BigDecimal>(queryList.size());
		for (Map m : queryList) {
			Long examPaperId = ((BigInteger) m.get("exam_paper_id")).longValue();
			BigDecimal difficulty = new BigDecimal(m.get("difficulty").toString());
			retMap.put(examPaperId, difficulty);
		}
		return retMap;
	}
}
