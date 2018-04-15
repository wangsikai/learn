package com.lanking.uxb.service.question.api.impl;

import java.util.Date;
import java.util.Map;

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
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.counter.api.impl.QuestionUserCouterProvider;
import com.lanking.uxb.service.question.api.QuestionUserCountStatService;

@Transactional(readOnly = true)
@Service
public class QuestionUserCountStatServiceImpl implements QuestionUserCountStatService {

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	private Repo<StudentHomeworkQuestion, Long> studentHkQuestionRepo;
	@Autowired
	private QuestionUserCouterProvider questionUserCouterProvider;
	@Autowired
	private MqSender mqSender;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void handleStu(Map map) {
		Long questionId = Long.parseLong(map.get("question_id").toString());
		Long studentId = Long.parseLong(map.get("student_id").toString());
		questionUserCouterProvider.counterDetail(questionId, studentId, Count.COUNTER_1, 1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void handleTea(Map map) {
		Long questionId = Long.parseLong(map.get("question_id").toString());
		Long teacherId = Long.parseLong(map.get("create_id").toString());
		questionUserCouterProvider.counterDetail(questionId, teacherId, Count.COUNTER_1, 1);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> stuCommonHkList(CursorPageable<Long> pageable, Date nowTime) {
		return studentHkQuestionRepo.find("$stuCommonHkList", Params.param("nowTime", nowTime)).fetch(pageable,
				Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> stuHolidayHkList(CursorPageable<Long> pageable, Date nowTime) {
		return studentHkQuestionRepo.find("$stuHolidayHkList", Params.param("nowTime", nowTime)).fetch(pageable,
				Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> stuOtherExerciseList(CursorPageable<Long> pageable, Date nowTime) {
		return studentHkQuestionRepo.find("$stuOtherExerciseList", Params.param("nowTime", nowTime)).fetch(pageable,
				Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> teaCommonHkList(CursorPageable<Long> pageable, Date nowTime) {
		return studentHkQuestionRepo.find("$teaCommonHkList", Params.param("nowTime", nowTime)).fetch(pageable,
				Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> teaHolidayHkList(CursorPageable<Long> pageable, Date nowTime) {
		return studentHkQuestionRepo.find("$teaHolidayHkList", Params.param("nowTime", nowTime)).fetch(pageable,
				Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> stuExerciseListByStuQuestionAnswerId(CursorPageable<Long> pageable) {
		return studentHkQuestionRepo.find("$stuExerciseListByStuQuestionAnswerId").fetch(pageable, Map.class,
				new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void stuExerciseMqSender(Map map) {
		Long questionId = Long.parseLong(map.get("question_id").toString());
		Long studentId = Long.parseLong(map.get("student_id").toString());
		JSONObject counterDetailJsonObject = new JSONObject();
		counterDetailJsonObject.put("bizId", questionId);
		counterDetailJsonObject.put("otherBizId", studentId);
		counterDetailJsonObject.put("count", Count.COUNTER_1);
		counterDetailJsonObject.put("delta", 1);
		mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
				MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
				MQ.builder().data(counterDetailJsonObject).build());
	}

}
