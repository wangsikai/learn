package com.lanking.uxb.rescon.exam.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.exam.api.ResconExamTopicManage;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;

@Transactional(readOnly = true)
@Service
public class ResconExamPaperTopicManageImpl implements ResconExamTopicManage {
	@Autowired
	@Qualifier("ExamPaperTopicRepo")
	private Repo<ExamPaperTopic, Long> examQuestionTopicRepo;
	@Autowired
	private ResconExamPaperQuestionManage examQuestionService;
	@Autowired
	private ResconQuestionManage questionService;

	@Override
	public List<ExamPaperTopic> getTopicsByExam(Long examId) {
		return examQuestionTopicRepo.find("$getTopicsByExam", Params.param("examId", examId)).list();
	}

	@Override
	public Map<ExamPaperTopicType, ExamPaperTopic> getTopicsMap(Long examId) {
		List<ExamPaperTopic> examTopicList = this.getTopicsByExam(examId);
		Map<ExamPaperTopicType, ExamPaperTopic> eptMap = Maps.newHashMap();
		for (ExamPaperTopic examPaperTopic : examTopicList) {
			eptMap.put(examPaperTopic.getType(), examPaperTopic);
		}
		return eptMap;
	}

	@Override
	public Map<Long, List<ExamPaperTopic>> mgetTopicsByExam(Collection<Long> keys) {
		List<ExamPaperTopic> tpList = examQuestionTopicRepo.find("$getTopicsByExams", keys).list();
		Map<Long, List<ExamPaperTopic>> topicMap = Maps.newHashMap();
		for (Long key : keys) {
			List<ExamPaperTopic> topicList = Lists.newArrayList();
			for (ExamPaperTopic examPaperTopic : tpList) {
				if (examPaperTopic.getExamPaperId().equals(key)) {
					topicList.add(examPaperTopic);
				}
			}
			topicMap.put(key, topicList);

		}
		return topicMap;
	}

	@Transactional
	@Override
	public void sortTopic(List<Long> examTopicIds) {
		Map<Long, ExamPaperTopic> examTopicMap = examQuestionTopicRepo.mget(examTopicIds);
		for (int i = 0; i < examTopicIds.size(); i++) {
			examTopicMap.get(examTopicIds.get(i)).setSequence(i);
		}
		examQuestionTopicRepo.save(examTopicMap.values());
	}

	@Transactional
	@Override
	public ExamPaperTopic edit(Long examTopicId, String examTopicName, Integer score) {
		Params params = Params.param();
		params.put("examTopicId", examTopicId);
		params.put("examTopicName", examTopicName);
		examQuestionTopicRepo.execute("$updateTopic", params);
		if (!examQuestionTopicRepo.get(examTopicId).getType().equals(ExamPaperTopicType.QUESTION_ANSWERING)) {
			// 更改试卷中改题型的分数
			examQuestionService.updateScoreByTopic(examTopicId, score);
		}
		return null;
	}

	@Transactional
	@Override
	public void updateByTopic(Long examId, List<VExamPaperTopic> topics) {
		List<ExamPaperTopic> epTopics = Lists.newArrayList();
		for (VExamPaperTopic vExamPaperTopic : topics) {
			ExamPaperTopic topic = new ExamPaperTopic();
			topic.setExamPaperId(examId);
			topic.setName(vExamPaperTopic.getName());
			topic.setSequence(vExamPaperTopic.getSequence());
			topic.setType(vExamPaperTopic.getType());
			epTopics.add(topic);
		}
		examQuestionTopicRepo.save(epTopics);

	}

	@Transactional
	@Override
	public List<ExamPaperTopic> save(List<VExamPaperTopic> topicList, Long examId) {
		// 删除试卷原有题目类型顺序
		List<Long> preTopicIds = Lists.newArrayList();
		List<ExamPaperTopic> topics = Lists.newArrayList();
		for (int i = 0; i < topicList.size(); i++) {
			preTopicIds.add(topicList.get(i).getId());
			ExamPaperTopic topic = new ExamPaperTopic();
			topic.setExamPaperId(examId);
			topic.setName(topicList.get(i).getName());
			topic.setSequence(i);
			topic.setType(topicList.get(i).getType());
			topics.add(topic);
		}
		examQuestionTopicRepo.deleteByIds(preTopicIds);
		return examQuestionTopicRepo.save(topics);
	}

}
