package com.lanking.uxb.service.question.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.question.cache.word.QuestionWordMLCacheService;

@Service
public class QuestionWordMLBatchAddService {
	@Autowired
	@Qualifier("QuestionWordMLDataRepo")
	private Repo<QuestionWordMLData, Long> repo;
	@Autowired
	private QuestionWordMLCacheService questionWordMLCacheService;

	@Transactional
	public void batchAddTransaction(Collection<Long> qids, List<QuestionWordMLData> questionWordMLDatas) {
		repo.execute("delete from question_wordml where id in (:ids)", Params.param("ids", qids));
		repo.save(questionWordMLDatas);
		questionWordMLCacheService.mutilSet(questionWordMLDatas);
	}
}
