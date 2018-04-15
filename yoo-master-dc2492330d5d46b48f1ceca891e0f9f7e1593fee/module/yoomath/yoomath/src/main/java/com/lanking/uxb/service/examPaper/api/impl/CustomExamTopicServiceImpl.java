package com.lanking.uxb.service.examPaper.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examPaper.api.CustomExamTopicService;

/**
 * @see CustomExamTopicService
 * @author zemin.song
 */
@Service
@Transactional(readOnly = true)
public class CustomExamTopicServiceImpl implements CustomExamTopicService {
	@Autowired
	@Qualifier("CustomExampaperTopicRepo")
	private Repo<CustomExampaperTopic, Long> customExamopicRepo;

	@Override
	public Map<CustomExampaperTopicType, CustomExampaperTopic> getTopicsMap(Long examPaperId) {
		List<CustomExampaperTopic> ceptList = this.getTopicsByExamPaperId(examPaperId);
		Map<CustomExampaperTopicType, CustomExampaperTopic> cepMap = Maps.newHashMap();
		for (CustomExampaperTopic cep : ceptList) {
			cepMap.put(cep.getType(), cep);
		}
		return cepMap;
	}

	@Override
	public List<CustomExampaperTopic> getTopicsByExamPaperId(Long examPaperId) {
		return customExamopicRepo.find("$getTopicsByExamPaperId", Params.param("examPaperId", examPaperId)).list();
	}

	@Override
	@Transactional
	public CustomExampaperTopic createCustomExampaperTopic(CustomExampaperTopic cet) {
		return customExamopicRepo.save(cet);
	}

}
