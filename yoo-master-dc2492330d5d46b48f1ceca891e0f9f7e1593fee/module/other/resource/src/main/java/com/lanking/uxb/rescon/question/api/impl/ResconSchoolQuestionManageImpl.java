package com.lanking.uxb.rescon.question.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;
import com.lanking.uxb.service.schoolQuestion.cache.SchoolQuestionCacheService;
import com.lanking.uxb.service.search.api.IndexService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ResconSchoolQuestionManageImpl implements ResconSchoolQuestionManage {

	@Autowired
	@Qualifier("SchoolQuestionRepo")
	Repo<SchoolQuestion, Long> schoolQuestionRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("QuestionSectionRepo")
	Repo<QuestionSection, Long> questionSectionRepo;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SchoolQuestionCacheService schoolQuestionCacheService;

	@Transactional
	@Override
	public void addSchoolQuestion(long schoolId, long questionId) {
		SchoolQuestion schoolQuestion = this.getSquestion(schoolId, questionId);
		// 如果已经存在，则不需要操作
		if (schoolQuestion == null) {
			Question question = questionRepo.get(questionId);
			schoolQuestion = new SchoolQuestion();
			schoolQuestion.setType(question.getType());
			schoolQuestion.setQuestionId(question.getId());
			schoolQuestion.setCreateAt(new Date());
			schoolQuestion.setDifficulty(question.getDifficulty());
			schoolQuestion.setSubjectCode(question.getSubjectCode());
			schoolQuestion.setSchoolId(schoolId);
			schoolQuestion.setTypeCode(question.getTypeCode());
			schoolQuestionRepo.save(schoolQuestion);
			indexService.syncUpdate(IndexType.SCHOOL_QUESTION, schoolQuestion.getId());
			this.updateSchoolCache(schoolId, questionId);
		}
	}

	@Transactional
	@Override
	public void delSchoolQuestion(long schoolId, long questionId) {
		SchoolQuestion sq = this.getSquestion(schoolId, questionId);
		if (null != sq) {
			schoolQuestionRepo.delete(sq);
			indexService.syncDelete(IndexType.SCHOOL_QUESTION, sq.getId());
		}
	}

	@Override
	public SchoolQuestion getSquestion(long schoolId, long questionId) {
		return schoolQuestionRepo.find("$getSquestion",
				Params.param("schoolId", schoolId).put("questionId", questionId)).get();
	}

	@Override
	public List<Integer> findByQuestionId(long questionId) {
		Params params = Params.param();
		params.put("questionId", questionId);
		List<QuestionSection> questionSections = questionSectionRepo.find("$zycFindByQuestionId", params).list();
		List<Integer> ids = Lists.newArrayList();
		for (QuestionSection q : questionSections) {
			ids.add(q.getTextBookCode());
		}
		return ids;
	}

	@Transactional
	@Override
	public void updateSchoolCache(Long schoolId, Long questionId) {
		List<Integer> textBookCodes = this.findByQuestionId(questionId);
		schoolQuestionCacheService.setTextbookFlag(schoolId, textBookCodes, "1");
	}

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void batchAddSchoolQuestions(List<Map> list) {
		for (Map map : list) {
			this.addSchoolQuestion(Long.parseLong((map.get("schoolId").toString())),
					Long.parseLong((map.get("questionId").toString())));
		}
	}

	@Override
	@Transactional
	public void delSchoolQuestion(long schoolId, List<Long> questionIds) {
		for (Long id : questionIds) {
			this.delSchoolQuestion(schoolId, id);
		}
	}

}
