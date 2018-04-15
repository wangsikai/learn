package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.domain.common.resource.question.Question2Category;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.question.api.ResconQuestionCategoryManage;

@Transactional(readOnly = true)
@Service
public class ResconQuestionCategoryManageImpl implements ResconQuestionCategoryManage {
	@Autowired
	@Qualifier("QuestionCategoryRepo")
	Repo<QuestionCategory, Long> questionCategoryRepo;
	@Autowired
	@Qualifier("Question2CategoryRepo")
	Repo<Question2Category, Long> question2CategoryRepo;

	@Override
	public Map<Long, QuestionCategory> mget(Collection<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Maps.newHashMap();
		}
		return questionCategoryRepo.mget(codes);
	}

	@Override
	public List<QuestionCategory> listAll(Status status) {
		Params params = Params.param();
		if (status != null) {
			params.put("status", status.getValue());
		}
		return questionCategoryRepo.find("$listAll", params).list();
	}

	@Override
	@Transactional
	public QuestionCategory saveOrUpdateCategory(Long code, String name, Status status) {
		QuestionCategory questionCategory = null;
		if (code == null) {
			questionCategory = new QuestionCategory();
		} else {
			questionCategory = questionCategoryRepo.get(code);
		}
		questionCategory.setName(name);
		questionCategory.setStatus(status);
		questionCategoryRepo.save(questionCategory);
		return questionCategory;
	}

	@Override
	public List<QuestionCategory> listByQuestion(long questionId) {
		return question2CategoryRepo.find("$listByQuestion", Params.param("questionId", questionId))
				.list(QuestionCategory.class);
	}

	@Override
	public Map<Long, List<QuestionCategory>> mgetByQuestions(Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Maps.newHashMap();
		}
		List<Question2Category> all = question2CategoryRepo
				.find("$listByQuestions", Params.param("questionIds", questionIds)).list();
		Map<Long, List<QuestionCategory>> map = new HashMap<Long, List<QuestionCategory>>(questionIds.size());

		Set<Long> catergoryCodes = new HashSet<Long>();
		for (Question2Category question2Category : all) {
			catergoryCodes.add(question2Category.getCategoryCode());
		}
		Map<Long, QuestionCategory> questionCategoryMap = this.mget(catergoryCodes);

		// 拼装
		for (Question2Category question2Category : all) {
			List<QuestionCategory> categorys = map.get(question2Category.getQuestionId());
			if (categorys == null) {
				categorys = new ArrayList<QuestionCategory>();
				map.put(question2Category.getQuestionId(), categorys);
			}
			QuestionCategory category = questionCategoryMap.get(question2Category.getCategoryCode());
			if (category != null) {
				categorys.add(category);
			}
		}

		return map;
	}
}
