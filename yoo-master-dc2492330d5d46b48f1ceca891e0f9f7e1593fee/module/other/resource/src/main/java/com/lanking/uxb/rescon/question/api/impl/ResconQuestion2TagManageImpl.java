package com.lanking.uxb.rescon.question.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.question.api.ResconQuestion2TagManage;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class ResconQuestion2TagManageImpl implements ResconQuestion2TagManage {

	@Autowired
	@Qualifier("Question2TagRepo")
	Repo<Question2Tag, Long> question2TagRepo;

	@Override
	@Transactional
	public void systemAdd(Collection<Long> questionIds, long tagCode) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return;
		}
		List<Question2Tag> question2Tags = question2TagRepo
				.find("$findByQuestionAndTag", Params.param("questionIds", questionIds).put("tagCode", tagCode)).list();
		if (CollectionUtils.isEmpty(question2Tags)) {
			for (Long questionId : questionIds) {
				Question2Tag question2Tag = new Question2Tag();
				question2Tag.setQuestionId(questionId);
				question2Tag.setSystem(true);
				question2Tag.setTagCode(tagCode);
				question2TagRepo.save(question2Tag);
			}
		} else {
			Map<Long, Question2Tag> q2tMap = new HashMap<Long, Question2Tag>(question2Tags.size());
			for (Question2Tag question2Tag : question2Tags) {
				question2Tag.setSystem(true);
				question2TagRepo.save(question2Tag);
				q2tMap.put(question2Tag.getQuestionId(), question2Tag);
			}

			for (Long questionId : questionIds) {
				if (q2tMap.get(questionId) == null) {
					Question2Tag question2Tag = new Question2Tag();
					question2Tag.setQuestionId(questionId);
					question2Tag.setSystem(true);
					question2Tag.setTagCode(tagCode);
					question2TagRepo.save(question2Tag);
				}
			}
		}
	}

	@Override
	@Transactional
	public void systemDel(Collection<Long> questionIds, long tagCode) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return;
		}
		List<Question2Tag> question2Tags = question2TagRepo
				.find("$findByQuestionAndTag", Params.param("questionIds", questionIds).put("tagCode", tagCode)).list();
		if (CollectionUtils.isNotEmpty(question2Tags)) {
			for (Question2Tag question2Tag : question2Tags) {
				question2TagRepo.delete(question2Tag);
			}
		}
	}
}
