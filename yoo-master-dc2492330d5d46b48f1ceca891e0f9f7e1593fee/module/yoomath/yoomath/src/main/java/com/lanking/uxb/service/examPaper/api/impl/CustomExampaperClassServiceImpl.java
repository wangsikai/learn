package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperClass;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.api.CustomExampaperClassService;

/**
 * @see CustomExampaperClassService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class CustomExampaperClassServiceImpl implements CustomExampaperClassService {
	@Autowired
	@Qualifier("CustomExampaperClassRepo")
	private Repo<CustomExampaperClass, Long> repo;

	@Override
	@Transactional
	public void create(long paperId, long classId) {
		CustomExampaperClass cec = new CustomExampaperClass();
		cec.setClassId(classId);
		cec.setCustomExampaperId(paperId);

		repo.save(cec);
	}

	@Override
	public List<Long> findByPaper(long paperId) {
		return repo.find("$findByPaper", Params.param("paperId", paperId)).list(Long.class);
	}

	@Override
	public Map<Long, List<Long>> findByPapers(Collection<Long> paperIds) {
		if (CollectionUtils.isEmpty(paperIds)) {
			return Collections.EMPTY_MAP;
		}
		List<CustomExampaperClass> list = repo.find("$findByPapers", Params.param("paperIds", paperIds)).list();
		Map<Long, List<Long>> retMap = new HashMap<Long, List<Long>>(paperIds.size());
		for (CustomExampaperClass c : list) {
			if (retMap.get(c.getCustomExampaperId()) == null) {
				retMap.put(c.getCustomExampaperId(), Lists.newArrayList(c.getClassId()));
			} else {
				retMap.get(c.getCustomExampaperId()).add(c.getClassId());
			}
		}

		return retMap;
	}
}
