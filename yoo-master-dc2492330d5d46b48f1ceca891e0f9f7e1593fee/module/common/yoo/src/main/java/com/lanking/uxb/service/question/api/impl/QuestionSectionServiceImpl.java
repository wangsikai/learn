package com.lanking.uxb.service.question.api.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.common.resource.question.QuestionSectionKey;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.QuestionSectionService;

@SuppressWarnings({ "rawtypes" })
@Transactional(readOnly = true)
@Service
public class QuestionSectionServiceImpl implements QuestionSectionService {

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, QuestionSectionKey> questionSectionRepo;

	@Override
	public List<QuestionSection> findByQuestionId(int version, long questionId) {
		return questionSectionRepo
				.find("$findByQuestionId", Params.param("questionId", questionId).put("version", version)).list();
	}

	@Override
	public Map<Long, Long> statisSectionQuestionCount(int version, Integer textbookCode,
			List<Integer> excludeQuestionTypeCodes) {
		Params params = Params.param("textbookCode", textbookCode).put("version", version);
		if (CollectionUtils.isNotEmpty(excludeQuestionTypeCodes)) {
			params.put("excludeQuestionTypeCodes", excludeQuestionTypeCodes);
		}
		List<Map> list = questionSectionRepo.find("$statisSectionQuestionCount", params).list(Map.class);
		Map<Long, Long> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				map.put(((BigInteger) m.get("section_code")).longValue(), ((BigInteger) m.get("cou")).longValue());
			}
		}
		return map;
	}
}
