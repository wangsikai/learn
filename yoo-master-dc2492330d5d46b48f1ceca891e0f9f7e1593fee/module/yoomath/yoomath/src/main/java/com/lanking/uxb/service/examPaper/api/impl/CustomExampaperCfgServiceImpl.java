package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;
import com.lanking.uxb.service.examPaper.api.CustomExampaperCfgService;

/**
 * @see CustomExampaperCfgService
 * @author zemin.song
 */
@Service
@Transactional(readOnly = true)
public class CustomExampaperCfgServiceImpl implements CustomExampaperCfgService {

	@Autowired
	@Qualifier("CustomExampaperCfgRepo")
	private Repo<CustomExampaperCfg, Long> repo;

	@Override
	public CustomExampaperCfg get(long examPaperId) {
		return repo.get(examPaperId);
	}

	@Override
	public Map<Long, CustomExampaperCfg> mget(Collection<Long> examPaperIds) {
		return repo.mget(examPaperIds);
	}

	@Override
	public void saveCustomExampaperCfg(CustomExampaperCfg cfg) {
		CustomExampaperCfg oldCfg = repo.get(cfg.getCustomExampaperId());
		if (oldCfg == null) {
			repo.save(cfg);
		} else {
			oldCfg.setFillBlankScore(cfg.getFillBlankScore());
			oldCfg.setSingleChoiceScore(cfg.getSingleChoiceScore());
			repo.save(oldCfg);
		}

	}

}
