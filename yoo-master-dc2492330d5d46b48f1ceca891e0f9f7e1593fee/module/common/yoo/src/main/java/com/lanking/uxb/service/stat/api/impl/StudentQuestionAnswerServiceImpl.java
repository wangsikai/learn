package com.lanking.uxb.service.stat.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathCounterDetailRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class StudentQuestionAnswerServiceImpl implements StudentQuestionAnswerService {

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	Repo<StudentQuestionAnswer, Long> stuQAnswerRepo;
	@Autowired
	private MqSender mqSender;

	@Transactional
	@Override
	public void create(long studentId, long questionId, Map<Long, List<String>> latexAnswers,
			Map<Long, List<String>> asciimathAnswers, List<Long> answerImgs, List<HomeworkAnswerResult> itemResults,
			Integer rightRate, HomeworkAnswerResult result, StudentQuestionAnswerSource source, Date createAt) {
		StudentQuestionAnswer sqa = new StudentQuestionAnswer();
		sqa.setStudentId(studentId);
		sqa.setQuestionId(questionId);
		sqa.setAnswers(latexAnswers);
		sqa.setAnswersAscii(asciimathAnswers);
		if (CollectionUtils.isNotEmpty(answerImgs)) {
			sqa.setAnswerImgs(answerImgs);
		}
		sqa.setRightRate(rightRate);
		sqa.setResult(result);
		sqa.setSource(source);
		sqa.setCreateAt(createAt);
		sqa.setItemResults(itemResults);
		stuQAnswerRepo.save(sqa);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sqaId", sqa.getId());
		jsonObject.put("questionId", questionId);
		jsonObject.put("studentId", studentId);
		jsonObject.put("answers", latexAnswers);
		jsonObject.put("answersAscii", asciimathAnswers);
		if (CollectionUtils.isNotEmpty(answerImgs)) {
			jsonObject.put("answerImgs", answerImgs);
		}
		jsonObject.put("itemResults", itemResults);
		jsonObject.put("rightRate", rightRate);
		jsonObject.put("result", result);
		jsonObject.put("source", source);
		jsonObject.put("createAt", createAt == null ? new Date() : createAt);

		// 统计学生做题次数(除了作业和假期作业,上述两种情况在学生提交的时候做此逻辑处理)
		if (source != null && source != StudentQuestionAnswerSource.QR && source != StudentQuestionAnswerSource.OCR
				&& source != StudentQuestionAnswerSource.HOMEWORK
				&& source != StudentQuestionAnswerSource.HOLIDAY_HOMEWORK) {
			JSONObject counterDetailJsonObject = new JSONObject();
			counterDetailJsonObject.put("bizId", questionId);
			counterDetailJsonObject.put("otherBizId", studentId);
			counterDetailJsonObject.put("count", Count.COUNTER_1);
			counterDetailJsonObject.put("delta", 1);
			mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
					MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
					MQ.builder().data(counterDetailJsonObject).build());
		}
		// 处理学生回答此题所关联章节及知识点掌握情况
		mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
				MqYoomathDataRegistryConstants.RK_YM_DATA_STUDENTEXERCISE, MQ.builder().data(jsonObject).build());
		mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
				MqYoomathDataRegistryConstants.RK_YM_DATA_STUDENTFALLIBLE, MQ.builder().data(jsonObject).build());
		// 临时(不能一个一个处理)
		List<String> asciiAnswersList = asciimathAnswers == null ? Collections.EMPTY_LIST
				: asciimathAnswers.get(String.valueOf(sqa.getQuestionId()));
		boolean done = false;
		if (asciiAnswersList != null) {
			for (String a : asciiAnswersList) {
				if (StringUtils.isNotBlank(a)) {
					done = true;
					break;
				}
			}
		}
		if (done) {
			mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
					MqYoomathDataRegistryConstants.RK_YM_DATA_DOQUESTIONGOAL, MQ.builder().data(jsonObject).build());
		}
	}

	@Async
	@Transactional
	@Override
	public void asynCreate(long studentId, List<Long> questionIds, List<Map<Long, List<String>>> latexAnswers,
			List<Map<Long, List<String>>> asciimathAnswers, List<List<Long>> answerImgs,
			List<List<HomeworkAnswerResult>> itemResults, List<Integer> rightRates, List<HomeworkAnswerResult> results,
			StudentQuestionAnswerSource source, Date createAt) {
		int size = questionIds.size();
		for (int i = 0; i < size; i++) {
			create(studentId, questionIds.get(i), latexAnswers.get(i), asciimathAnswers.get(i),
					answerImgs == null ? null : answerImgs.get(i), itemResults == null ? null : itemResults.get(i),
					rightRates == null ? null : rightRates.get(i), results.get(i), source, createAt);
		}
	}

	@Override
	public List<StudentQuestionAnswer> findByQuestionId(long studentId, long questionId, int limit) {
		return stuQAnswerRepo.find("$zyFindByQuestionId",
				Params.param("studentId", studentId).put("questionId", questionId).put("limit", limit)).list();
	}

	@Override
	public List<StudentQuestionAnswer> findByQuestionIdGroup(long studentId, int limit) {
		Params params = Params.param("studentId", studentId).put("limit", limit);
		return stuQAnswerRepo.find("$zyFindByQuestionIdGroup", params).list();
	}

	@Override
	public List<StudentQuestionAnswer> findByQuestionIdGroup(long studentId, Collection<Long> questionIds, int limit) {
		Params params = Params.param("studentId", studentId).put("limit", limit);
		if (null != questionIds && questionIds.size() > 0) {
			params.put("questionIds", questionIds);
		}
		return stuQAnswerRepo.find("$zyFindByQuestionIdGroup", params).list();
	}

	@Override
	public List<Map> findStudentCondition(long studentId, Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Collections.EMPTY_LIST;
		}
		return stuQAnswerRepo
				.find("$zyFindStudentCondition", Params.param("studentId", studentId).put("questionIds", questionIds))
				.list(Map.class);
	}

	@Override
	@Transactional
	public void asynCreateExamActivity(long studentId, List<Long> questionIds,
			List<Map<Long, List<String>>> latexAnswers, List<Map<Long, List<String>>> asciimathAnswers,
			List<List<Long>> answerImgs, List<List<HomeworkAnswerResult>> itemResults, List<Integer> rightRates,
			List<HomeworkAnswerResult> results, StudentQuestionAnswerSource source, Date createAt) {
		int size = questionIds.size();
		for (int i = 0; i < size; i++) {
			createExamActivity(studentId, questionIds.get(i), latexAnswers.get(i), asciimathAnswers.get(i),
					answerImgs == null ? null : answerImgs.get(i), itemResults == null ? null : itemResults.get(i),
					rightRates == null ? null : rightRates.get(i), results.get(i), source, createAt);
		}
	}
	
	@Transactional
	public void createExamActivity(long studentId, long questionId, Map<Long, List<String>> latexAnswers,
			Map<Long, List<String>> asciimathAnswers, List<Long> answerImgs, List<HomeworkAnswerResult> itemResults,
			Integer rightRate, HomeworkAnswerResult result, StudentQuestionAnswerSource source, Date createAt) {
		StudentQuestionAnswer sqa = new StudentQuestionAnswer();
		sqa.setStudentId(studentId);
		sqa.setQuestionId(questionId);
		sqa.setAnswers(latexAnswers);
		sqa.setAnswersAscii(asciimathAnswers);
		if (CollectionUtils.isNotEmpty(answerImgs)) {
			sqa.setAnswerImgs(answerImgs);
		}
		sqa.setRightRate(rightRate);
		sqa.setResult(result);
		sqa.setSource(source);
		sqa.setCreateAt(createAt);
		sqa.setItemResults(itemResults);
		stuQAnswerRepo.save(sqa);
	}
}
