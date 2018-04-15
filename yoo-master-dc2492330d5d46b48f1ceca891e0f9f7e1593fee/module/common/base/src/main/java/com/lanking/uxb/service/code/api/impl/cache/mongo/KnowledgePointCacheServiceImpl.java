package com.lanking.uxb.service.code.api.impl.cache.mongo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.impl.cache.local.AbstractBaseDataHandle;
import com.lanking.uxb.service.mongodb.api.LddpMongoTemplate;

@Service
@ConditionalOnExpression("${common.code.cache}")
public class KnowledgePointCacheServiceImpl extends AbstractBaseDataHandle implements KnowledgePointService {

	@Autowired
	@Qualifier("KnowledgePointRepo")
	private Repo<KnowledgePoint, Long> pointRepo;

	@Autowired
	private LddpMongoTemplate template;

	@Override
	public KnowledgePoint get(Long code) {
		return template.$findById(code, KnowledgePoint.class);
	}

	@Override
	public Map<Long, KnowledgePoint> mget(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgePoint> list = template.$find(query, KnowledgePoint.class);
		Map<Long, KnowledgePoint> map = new HashMap<Long, KnowledgePoint>();
		for (KnowledgePoint kp : list) {
			map.put(kp.getCode(), kp);
		}
		return map;
	}

	@Override
	public List<KnowledgePoint> mgetList(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgePoint> list = template.$find(query, KnowledgePoint.class);
		return list;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public List<KnowledgePoint> findBySubject(Integer subjectCode) {
		Query query = new Query();
		Criteria criteria = Criteria.where("subjectCode").is(subjectCode);
		query.addCriteria(criteria);
		List<KnowledgePoint> list = template.$find(query, KnowledgePoint.class);
		return list;
	}

	@Override
	public List<KnowledgePoint> findByPcode(Long pcode) {
		Query query = new Query();
		Criteria criteria = Criteria.where("pcode").is(pcode);
		query.addCriteria(criteria);
		List<KnowledgePoint> list = template.$find(query, KnowledgePoint.class);
		return list;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.KNOWLEDGEPOINT;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		template.$dropCollection(KnowledgePoint.class);
		if (CollectionUtils.isEmpty(template.$findAll(KnowledgePoint.class))) {
			List<KnowledgePoint> list = pointRepo.find("$findAllPoint").list();
			template.$insertAll(list);
		}
	}

	@Override
	public List<KnowledgePoint> findAll(Collection<Long> noHasCodes) {
		Query query = new Query();
		if (CollectionUtils.isNotEmpty(noHasCodes)) {
			Criteria criteria = Criteria.where("code").nin(noHasCodes);
			query.addCriteria(criteria);
		}
		return template.$find(query, KnowledgePoint.class);
	}
}
