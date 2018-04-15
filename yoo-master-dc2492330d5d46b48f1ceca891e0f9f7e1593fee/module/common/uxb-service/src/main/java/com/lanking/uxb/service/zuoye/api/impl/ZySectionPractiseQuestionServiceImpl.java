package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractiseQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseQuestionService;
import com.lanking.uxb.service.zuoye.form.SectionPractiseQuestionForm;

/**
 * @see ZySectionPractiseQuestionService
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
@Service
@Transactional(readOnly = true)
public class ZySectionPractiseQuestionServiceImpl implements ZySectionPractiseQuestionService {
	@Autowired
	@Qualifier("SectionPractiseQuestionRepo")
	Repo<SectionPractiseQuestion, Long> repo;

	@Override
	public List<SectionPractiseQuestion> mgetListByPractise(long practiseId) {
		return repo.find("$mgetListByPractise", Params.param("practiseId", practiseId)).list();
	}

	@Override
	@Transactional
	public void draft(List<SectionPractiseQuestionForm> forms, long practiseId) {
		List<SectionPractiseQuestion> questions = mgetListByPractise(practiseId);
		boolean hasDo = CollectionUtils.isNotEmpty(questions);
		Map<Long, SectionPractiseQuestion> questionMap = null;
		if (hasDo) {
			questionMap = new HashMap<Long, SectionPractiseQuestion>(questions.size());
			for (SectionPractiseQuestion spq : questions) {
				questionMap.put(spq.getQuestionId(), spq);
			}
		}
		for (SectionPractiseQuestionForm form : forms) {
			SectionPractiseQuestion q = null;
			if (hasDo) {
				q = questionMap.get(form.getQuestionId());
				q.setUpdateAt(new Date());
			} else {
				q = new SectionPractiseQuestion();
				q.setCreateAt(new Date());
			}
			q.setAnswer(form.getAnswers());
			q.setDone(form.isDone());
			q.setPractiseId(practiseId);
			q.setQuestionId(form.getQuestionId());

			repo.save(q);
		}
	}

	@Override
	@Transactional
	public void commit(List<SectionPractiseQuestionForm> forms, long practiseId) {
		List<SectionPractiseQuestion> questions = mgetListByPractise(practiseId);
		boolean hasDo = CollectionUtils.isNotEmpty(questions);
		Map<Long, SectionPractiseQuestion> questionMap = null;
		if (hasDo) {
			questionMap = new HashMap<Long, SectionPractiseQuestion>(questions.size());
			for (SectionPractiseQuestion spq : questions) {
				questionMap.put(spq.getQuestionId(), spq);
			}
		}
		for (SectionPractiseQuestionForm form : forms) {
			SectionPractiseQuestion question = null;
			if (hasDo) {
				question = questionMap.get(form.getQuestionId());
				question.setUpdateAt(new Date());
			} else {
				question = new SectionPractiseQuestion();
				question.setCreateAt(new Date());
			}
			question.setAnswer(form.getAnswers());
			question.setPractiseId(practiseId);
			question.setQuestionId(form.getQuestionId());
			question.setDone(form.isDone());
			question.setResult(form.getResult());

			repo.save(question);
		}
	}

}
