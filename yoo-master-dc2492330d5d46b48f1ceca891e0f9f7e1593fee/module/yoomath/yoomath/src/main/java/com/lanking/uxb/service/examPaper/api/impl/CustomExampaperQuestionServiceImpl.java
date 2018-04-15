package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examPaper.api.CustomExamTopicService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperQuestionService;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperForm;

/**
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class CustomExampaperQuestionServiceImpl implements CustomExampaperQuestionService {

	@Autowired
	@Qualifier("CustomExampaperQuestionRepo")
	private Repo<CustomExampaperQuestion, Long> repo;

	@Autowired
	private CustomExamTopicService customExamTopicService;

	@Override
	public List<CustomExampaperQuestion> findByPaper(long examPaperId) {
		return repo.find("$findByPaper", Params.param("paperId", examPaperId)).list();
	}

	@Override
	@Transactional
	public void updateCustomExamQuesions(CustomExamPaperForm form) {
		long examPaperId = form.getId();
		Date date = new Date();
		// 清空试卷问题
		repo.execute("$deletCustomExamQuestions", Params.param("examPaperId", examPaperId));
		// 获取当前试卷对应题题型
		Map<CustomExampaperTopicType, CustomExampaperTopic> topicMap = customExamTopicService.getTopicsMap(examPaperId);
		List<CustomExampaperQuestion> saveQuestions = Lists.newArrayList();
		int indeTop = -1;
		for (int topicTypeCount = 0; topicTypeCount < 3; topicTypeCount++) {
			List<CustomExampaperQuestion> questions = Lists.newArrayList();
			CustomExampaperTopic cet = null;
			// 单选
			if (topicTypeCount == CustomExampaperTopicType.SINGLE_CHOICE.getValue()) {
				questions = form.getSingleQuestions();
				cet = topicMap.get(CustomExampaperTopicType.SINGLE_CHOICE);
			}
			// 填空
			if (topicTypeCount == CustomExampaperTopicType.FILL_BLANK.getValue()) {
				questions = form.getFillQuestions();
				cet = topicMap.get(CustomExampaperTopicType.FILL_BLANK);
			}
			// 解答
			if (topicTypeCount == CustomExampaperTopicType.QUESTION_ANSWERING.getValue()) {
				questions = form.getAnswerQuestions();
				cet = topicMap.get(CustomExampaperTopicType.QUESTION_ANSWERING);
			}
			// 判断是否存在类型
			if (questions.size() > 0 && null == cet) {
				indeTop += 1;
				cet = new CustomExampaperTopic();
				cet.setCustomExampaperId(examPaperId);
				// 别名选择
				if (topicTypeCount == CustomExampaperTopicType.SINGLE_CHOICE.getValue()) {
					cet.setName(CustomExampaperTopicType.SINGLE_CHOICE.getName());
					cet.setType(CustomExampaperTopicType.SINGLE_CHOICE);
				}
				if (topicTypeCount == CustomExampaperTopicType.FILL_BLANK.getValue()) {
					cet.setName(CustomExampaperTopicType.FILL_BLANK.getName());
					cet.setType(CustomExampaperTopicType.FILL_BLANK);
				}
				if (topicTypeCount == CustomExampaperTopicType.QUESTION_ANSWERING.getValue()) {
					cet.setName(CustomExampaperTopicType.QUESTION_ANSWERING.getName());
					cet.setType(CustomExampaperTopicType.QUESTION_ANSWERING);
				}
				cet.setSequence(indeTop);
				// 创建TOPIC
				cet = customExamTopicService.createCustomExampaperTopic(cet);
			}
			// 存储题目
			for (CustomExampaperQuestion ceq : questions) {
				ceq.setCreateAt(date);
				ceq.setCustomExampaperId(examPaperId);
				ceq.setCustomExampaperTopicId(cet.getId());
				ceq.setType(cet.getType());
				saveQuestions.add(ceq);
			}
		}
		repo.save(saveQuestions);
	}
}
