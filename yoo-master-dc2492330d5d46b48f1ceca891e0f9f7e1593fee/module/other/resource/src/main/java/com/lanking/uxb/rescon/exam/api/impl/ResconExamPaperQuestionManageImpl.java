package com.lanking.uxb.rescon.exam.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.exam.api.ResconExamTopicManage;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;

@Transactional(readOnly = true)
@Service
public class ResconExamPaperQuestionManageImpl implements ResconExamPaperQuestionManage {
	@Autowired
	@Qualifier("ExamPaperQuestionRepo")
	private Repo<ExamPaperQuestion, Long> examQuestionRepo;
	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> examRepo;

	@Autowired
	private ResconExamTopicManage topicService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;

	@Transactional
	@Override
	public void updateExamQuesions(Long examId, List<Long> qIds, Long createId, Map<Long, VExamPaperTopic> vmap,
			Map<String, Integer> examTopicScoreMap) {
		List<ExamPaperQuestion> epList = this.getExamQuestionByExam(examId);
		Map<Long, Long> map = Maps.newHashMap();
		Map<Long, Integer> scoreMap = Maps.newHashMap();
		for (ExamPaperQuestion examPaperQuestion : epList) {
			map.put(examPaperQuestion.getQuestionId(), examPaperQuestion.getTopicId());
			scoreMap.put(examPaperQuestion.getQuestionId(), examPaperQuestion.getScore());
		}
		examQuestionRepo.execute("$deletExamQuestions", Params.param("examId", examId));
		List<ExamPaperQuestion> epqList = Lists.newArrayList();
		Date d = new Date();
		Map<ExamPaperTopicType, ExamPaperTopic> examTopicMap = topicService.getTopicsMap(examId);
		for (int i = 0; i < qIds.size(); i++) {
			ExamPaperQuestion epq = new ExamPaperQuestion();
			epq.setCreateAt(d);
			epq.setCreateId(createId);
			epq.setExamPaperId(examId);
			epq.setQuestionId(qIds.get(i));
			epq.setSequence(i);
			// 新加入的题目
			if (vmap.get(qIds.get(i)) != null) {
				long id = examTopicMap.get(vmap.get(qIds.get(i)).getType()).getId();
				epq.setTopicId(id);
				if (!examTopicMap.get(vmap.get(qIds.get(i)).getType()).getType()
						.equals(ExamPaperTopicType.QUESTION_ANSWERING)) {
					epq.setScore(examTopicScoreMap.get(String.valueOf(id)));
				}
			} else {
				epq.setTopicId(map.get(qIds.get(i)));
				epq.setScore(scoreMap.get(qIds.get(i)));
			}
			epqList.add(epq);
		}
		examQuestionRepo.save(epqList);
	}

	@Override
	public List<ExamPaperQuestion> getExamQuestionByExam(Long examId) {
		return examQuestionRepo.find("$getExamPaperByExamId", Params.param("examId", examId)).list();
	}

	@Override
	public void updateScoreByTopic(Long examTopicId, Integer score) {
		examQuestionRepo.execute("$updateScoreByTopic", Params.param("examTopicId", examTopicId).put("score", score));

	}

	@Transactional
	@Override
	public void save(List<VExamPaperTopic> topicList, Long examId, long userId) {
		List<Long> questionIds = Lists.newArrayList();
		List<ExamPaperQuestion> questionList = Lists.newArrayList();
		for (VExamPaperTopic vExamPaperTopic : topicList) {
			for (int i = 0; i < vExamPaperTopic.getQuestionList().size(); i++) {
				ExamPaperQuestion examQuestion = new ExamPaperQuestion();
				examQuestion.setCreateAt(new Date());
				examQuestion.setCreateId(userId);
				examQuestion.setExamPaperId(examId);
				examQuestion.setQuestionId(vExamPaperTopic.getQuestionList().get(i).getId());
				examQuestion.setScore(vExamPaperTopic.getQuestionList().get(i).getScore());
				examQuestion.setSequence(i);
				examQuestion.setTopicId(vExamPaperTopic.getId());
				questionIds.add(vExamPaperTopic.getQuestionList().get(i).getId());
				questionList.add(examQuestion);
			}
		}
		// 设置删除的习题学校属性
		ExamPaper examPaper = examRepo.get(examId);
		if (examPaper.getOwnSchoolId() != null) {
			List<ExamPaperQuestion> oldExamQuestions = this.getExamQuestionByExam(examId);
			Map<Long, Long> oldQuestionIds = new HashMap<Long, Long>();
			for (ExamPaperQuestion pq : oldExamQuestions) {
				oldQuestionIds.put(pq.getQuestionId(), pq.getQuestionId());
			}
			for (ExamPaperQuestion pq : questionList) {
				oldQuestionIds.remove(pq.getQuestionId());
			}
			Set<Long> deleteQuestions = oldQuestionIds.keySet();
			if (deleteQuestions.size() > 0) {
				examQuestionRepo.execute("$removeQuestionSchool", Params.param("questionIds", deleteQuestions));

				List<Question> qs = questionManage.mgetList(deleteQuestions);
				List<Long> qss = new ArrayList<Long>(qs.size());
				for (Question question : qs) {
					// 只有已通过的题目进行校本题目计数
					if (question.getStatus() == CheckStatus.PASS) {
						qss.add(question.getId());
					}
				}
				if (qss.size() > 0) {
					questionManage.updateQuestionSchoolCount(examPaper.getOwnSchoolId(), -qss.size());
					resconSchoolQuestionManage.delSchoolQuestion(examPaper.getOwnSchoolId(), qss);
				}
			}
		}

		examQuestionRepo.execute("$deletExamQuestions", Params.param("examId", examId));
		examQuestionRepo.save(questionList);

	}

	@Transactional
	@Override
	public void editScore(Long examId, Long questionId, Integer score) {
		examQuestionRepo.execute("$editScore",
				Params.param("examId", examId).put("questionId", questionId).put("score", score));
	}

	@Transactional
	@Override
	public void deleteQuestionFromExam(Long questionId) {
		examQuestionRepo.execute("$deleteQuestionFromExam", Params.param("questionId", questionId));
	}
}
