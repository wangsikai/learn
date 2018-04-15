package com.lanking.uxb.service.question.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.stat.KnowledgeQuestionStat;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.question.api.KnowledgeQuestionCountStatService;

@Transactional(readOnly = true)
@Service
public class KnowledgeQuestionCountStatServiceImpl implements KnowledgeQuestionCountStatService {

	@Autowired
	@Qualifier("MetaKnowpointRepo")
	private Repo<MetaKnowpoint, Long> metaKnowpointRepo;

	@Autowired
	@Qualifier("KnowledgePointRepo")
	private Repo<KnowledgePoint, Long> knowledgePointRepo;

	@Autowired
	@Qualifier("KnowledgeQuestionStatRepo")
	private Repo<KnowledgeQuestionStat, Long> statRepo;

	@Override
	public CursorPage<Integer, MetaKnowpoint> queryKnowpoint(CursorPageable<Integer> pageable) {
		return metaKnowpointRepo.find("$findKnowpoint").fetch(pageable, MetaKnowpoint.class,
				new CursorGetter<Integer, MetaKnowpoint>() {
					@Override
					public Integer getCursor(MetaKnowpoint bean) {
						return bean.getCode();
					}
				});
	}

	@Transactional
	@Override
	public void deleteKnowpoint() {
		statRepo.execute("$deleteknowledgeQuestionStat");
	}

	@Override
	public CursorPage<Long, KnowledgePoint> queryKnowledgePoint(CursorPageable<Long> pageable) {
		return knowledgePointRepo.find("$findKnowledgePoint").fetch(pageable, KnowledgePoint.class,
				new CursorGetter<Long, KnowledgePoint>() {
					public Long getCursor(KnowledgePoint bean) {
						return bean.getCode();
					}
				});
	}

	@Override
	public List<Map> getCountData(List<Integer> codes) {
		return statRepo.find("$getKnowPoint", Params.param("code", codes)).list(Map.class);
	}

	@Override
	public List<Map> getCountNewData(List<Long> codes) {
		return statRepo.find("$getNewKnowPoint", Params.param("code", codes)).list(Map.class);
	}

	@Transactional
	@Override
	public void saveKnowledgeQuestionStat(List<KnowledgeQuestionStat> kqsList) {
		statRepo.save(kqsList);
	}

}
