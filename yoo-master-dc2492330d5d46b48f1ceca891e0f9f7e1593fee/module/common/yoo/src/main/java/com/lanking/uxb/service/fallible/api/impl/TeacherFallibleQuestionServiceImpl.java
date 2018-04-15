package com.lanking.uxb.service.fallible.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.fallible.api.TeacherFallibleQuestionService;
import com.lanking.uxb.service.fallible.cache.TeacherFallibleCacheService;
import com.lanking.uxb.service.question.api.QuestionSectionService;

@Transactional(readOnly = true)
@Service
public class TeacherFallibleQuestionServiceImpl implements TeacherFallibleQuestionService {

	@Autowired
	@Qualifier("TeacherFallibleQuestionRepo")
	Repo<TeacherFallibleQuestion, Long> teacherFallibleRepo;

	@Autowired
	private QuestionSectionService questionSectionService;
	@Autowired
	private TeacherFallibleCacheService teaFallCacheService;

	@Transactional
	@Override
	public long update(long teacherId, long questionId, HomeworkAnswerResult result, Integer subjectCode, Type type,
			Integer typeCode, Double difficulty) {
		TeacherFallibleQuestion p = teacherFallibleRepo
				.find("$find", Params.param("teacherId", teacherId).put("questionId", questionId)).get();
		if (p == null) {
			p = new TeacherFallibleQuestion();
			p.setCreateAt(new Date());
			p.setDoNum(1);
			p.setQuestionId(questionId);
			p.setRightNum(result == HomeworkAnswerResult.RIGHT ? 1 : 0);
			p.setDoNum(1);
			p.setTeacherId(teacherId);
		} else {
			p.setDoNum(p.getDoNum() + 1);
			if (result == HomeworkAnswerResult.RIGHT) {
				p.setRightNum(p.getRightNum() + 1);
			}
		}
		p.setRightRate(
				BigDecimal.valueOf((p.getRightNum() * 100f) / (p.getDoNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
		p.setType(type);
		p.setTypeCode(typeCode);
		p.setSubjectCode(subjectCode);
		p.setDifficulty(difficulty);
		p.setUpdateAt(new Date());
		// since v1.3.0 已删除错题满足正确率小于50继续加入错题本 戚元鹏让这样修改
		if (p.getRightRate().compareTo(BigDecimal.valueOf(50)) < 0) {
			p.setStatus(Status.ENABLED);
		}
		teacherFallibleRepo.save(p);
		// update fallible cache
		List<QuestionSection> list = questionSectionService.findByQuestionId(1, questionId);
		for (QuestionSection questionSection : list) {
			teaFallCacheService.setTextbookFlag(teacherId, questionSection.getTextBookCode(), "1");
		}
		return p.getId();
	}

}
