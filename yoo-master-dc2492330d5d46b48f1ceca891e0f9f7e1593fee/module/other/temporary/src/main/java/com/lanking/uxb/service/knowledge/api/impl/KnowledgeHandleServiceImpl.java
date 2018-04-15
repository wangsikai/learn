package com.lanking.uxb.service.knowledge.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.knowledge.api.KnowledgeHandleService;

@Transactional(readOnly = true)
@Service
public class KnowledgeHandleServiceImpl implements KnowledgeHandleService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> kpRepo;

	@Override
	public List<Long> findKpIsNullList() {
		return kpRepo.find("$findKpIsNullList").list(Long.class);
	}

	@Override
	public List<Long> findNewKps(long homeworkId) {
		return kpRepo.find("$findNewKps", Params.param("homeworkId", homeworkId)).list(Long.class);
	}

	@Transactional
	@Override
	public void updateKp(long homeworkId, List<Long> kps) {
		Homework hk = kpRepo.get(homeworkId);
		hk.setQuestionknowledgePoints(kps);
		// 说明是旧知识点存到新知识点里面了，这里置空处理
		if (CollectionUtils.isNotEmpty(hk.getKnowledgePoints())) {
			hk.setKnowledgePoints(null);
		}
		kpRepo.save(hk);
	}

	@Override
	public List<Long> findKpIsWrongList() {
		return kpRepo.find("$findKpIsWrongList").list(Long.class);
	}
}
