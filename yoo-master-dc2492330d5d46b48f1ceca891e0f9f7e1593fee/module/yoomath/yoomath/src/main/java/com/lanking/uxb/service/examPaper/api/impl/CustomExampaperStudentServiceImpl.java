package com.lanking.uxb.service.examPaper.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathCounterDetailRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudent;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudentQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudentStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examPaper.api.CustomExampaperQuestionService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperStudentService;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuery;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuestionStatisticForm;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;

/**
 * @see CustomExampaperStudentService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class CustomExampaperStudentServiceImpl implements CustomExampaperStudentService {
	@Autowired
	@Qualifier("CustomExampaperStudentRepo")
	private Repo<CustomExampaperStudent, Long> repo;
	@Autowired
	@Qualifier("CustomExampaperStudentQuestionRepo")
	private Repo<CustomExampaperStudentQuestion, Long> cesQuestionRepo;

	@Autowired
	private CustomExampaperQuestionService exampaperQuestionService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private MqSender mqSender;

	@Override
	@Transactional
	public void open(Collection<HomeworkStudentClazz> homeworkStudentClazzs, long paperId) {
		Date now = new Date();

		List<CustomExampaperQuestion> questions = exampaperQuestionService.findByPaper(paperId);
		for (HomeworkStudentClazz hsc : homeworkStudentClazzs) {
			CustomExampaperStudent s = new CustomExampaperStudent();
			s.setCreateAt(now);
			s.setCustomExampaperId(paperId);
			s.setClassId(hsc.getClassId());
			s.setStatus(CustomExampaperStudentStatus.OPEN);
			s.setStudentId(hsc.getStudentId());

			repo.save(s);

			for (CustomExampaperQuestion q : questions) {
				CustomExampaperStudentQuestion studentQuestion = new CustomExampaperStudentQuestion();
				studentQuestion.setCustomExampaperId(paperId);
				studentQuestion.setCustomExampaperQuestionId(q.getId());
				studentQuestion.setCustomExampaperStudentId(s.getId());
				studentQuestion.setQuestionId(q.getQuestionId());

				cesQuestionRepo.save(studentQuestion);
			}
		}
	}

	@Override
	public Page<CustomExampaperStudent> query(CustomExampaperStudentQuery query, Pageable pageable) {
		Params params = Params.param("studentID", query.getStudentId());
		if (null != query) {
			if (query.getClazzID() != null) {
				params.put("clazzID", query.getClazzID());
			}
			if (StringUtils.isNotEmpty(query.getKey())) {
				params.put("key0", "%" + query.getKey() + "%");
			}
			if (query.getBt() != null) {
				params.put("bt", new Date(query.getBt()));
			}
			if (query.getEt() != null) {
				params.put("et", new Date(query.getEt()));
			}
		}
		return repo.find("$queryCustomExampaperStudent", params).fetch(pageable);
	}

	@Override
	public long countNewDatas(long studentID, long timestamp) {
		return repo.find("$countNewDatas", Params.param("cutTime", new Date(timestamp)).put("studentID", studentID))
				.count();
	}

	@Override
	public List<CustomExampaperTopic> findCustomExampaperTopicByStudent(long customExampaperStudentID) {
		return repo
				.find("$findCustomExampaperTopicByStudent",
						Params.param("customExampaperStudentID", customExampaperStudentID))
				.list(CustomExampaperTopic.class);
	}

	@Override
	public CustomExampaperStudent get(long id) {
		return repo.get(id);
	}

	@Override
	public List<CustomExampaperStudentQuestion> listStudentPaperQuestionByCustomExampaperStudentID(
			long customExampaperStudentID) {
		return cesQuestionRepo.find("$listCustomExampaperStudentQuestions",
				Params.param("customExampaperStudentID", customExampaperStudentID)).list();
	}

	@Override
	public List<CustomExampaperQuestion> listPaperQuestionByCustomExampaperStudentID(long customExampaperStudentID) {
		return cesQuestionRepo
				.find("$listCustomExampaperQuestions",
						Params.param("customExampaperStudentID", customExampaperStudentID))
				.list(CustomExampaperQuestion.class);
	}

	@Override
	@Transactional
	public void submitStatistic(Long customExampaperStudentId,
			List<CustomExampaperStudentQuestionStatisticForm> statistics) {

		CustomExampaperStudent paper = repo.get(customExampaperStudentId);
		List<Long> stuQuesitonIDs = new ArrayList<Long>(statistics.size());
		for (CustomExampaperStudentQuestionStatisticForm form : statistics) {
			stuQuesitonIDs.add(form.getStuQuestionId());
		}
		Map<Long, CustomExampaperStudentQuestion> stuQuestionMap = cesQuestionRepo.mget(stuQuesitonIDs);
		List<StudentFallibleQuestion> sfQuestions = new ArrayList<StudentFallibleQuestion>(); // 错题集合
		for (CustomExampaperStudentQuestionStatisticForm form : statistics) {
			CustomExampaperStudentQuestion stuQuestion = stuQuestionMap.get(form.getStuQuestionId());
			if (form.getResult() == HomeworkAnswerResult.WRONG) {
				stuQuestion.setResult(HomeworkAnswerResult.WRONG);

				// 添加错题
				StudentFallibleQuestion sfQuestion = new StudentFallibleQuestion();
				sfQuestion.setQuestionId(stuQuestion.getQuestionId());
				sfQuestion.setStudentId(paper.getStudentId());
				sfQuestions.add(sfQuestion);

				// @since 2017-02-21 学生组卷统计的错题，记录到新的统计表中
				JSONObject counterDetailJsonObject = new JSONObject();
				counterDetailJsonObject.put("bizId", stuQuestion.getQuestionId());
				counterDetailJsonObject.put("otherBizId", paper.getStudentId());
				counterDetailJsonObject.put("count", Count.COUNTER_1);
				counterDetailJsonObject.put("delta", 1);
				mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
						MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
						MQ.builder().data(counterDetailJsonObject).build());
			} else {
				stuQuestion.setResult(HomeworkAnswerResult.RIGHT);
			}
		}
		cesQuestionRepo.save(stuQuestionMap.values());

		// 组卷
		paper.setStatisticsAt(new Date());
		paper.setStatus(CustomExampaperStudentStatus.STATISTICS);
		repo.save(paper);

		// 错题
		sfqService.updateFromStudentCustompaper(sfQuestions);
	}
}
