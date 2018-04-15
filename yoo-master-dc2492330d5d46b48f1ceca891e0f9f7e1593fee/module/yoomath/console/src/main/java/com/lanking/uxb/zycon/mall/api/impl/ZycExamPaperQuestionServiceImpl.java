package com.lanking.uxb.zycon.mall.api.impl;

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
import com.lanking.uxb.zycon.mall.api.ZycExamPaperQuestionService;

/**
 * @see ExamPaperQuestionService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ZycExamPaperQuestionServiceImpl implements ZycExamPaperQuestionService {

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
		List<Map> queryList = repo.find("$zycFindExamQuestionCount", Params.param("examIds", examIds)).list(Map.class);
		Map<Long, Integer> retMap = new HashMap<Long, Integer>(queryList.size());
		for (Map m : queryList) {
			Long examPaperId = ((BigInteger) m.get("exam_paper_id")).longValue();
			Integer questionCount = ((BigInteger) m.get("c")).intValue();

			retMap.put(examPaperId, questionCount);
		}

		return retMap;
	}
}
