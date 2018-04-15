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
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.KnowledgeSyncService;
import com.lanking.uxb.service.code.api.impl.cache.local.AbstractBaseDataHandle;
import com.lanking.uxb.service.mongodb.api.LddpMongoTemplate;

@Service
@ConditionalOnExpression("${common.code.cache}")
public class KnowledgeSyncCacheServiceImpl extends AbstractBaseDataHandle implements KnowledgeSyncService {
	@Autowired
	@Qualifier("KnowledgeSyncRepo")
	private Repo<KnowledgeSync, Long> knowledgeSyncRepo;

	@Autowired
	private LddpMongoTemplate template;

	@Override
	public Map<Long, KnowledgeSync> mget(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgeSync> list = template.$find(query, KnowledgeSync.class);
		Map<Long, KnowledgeSync> map = new HashMap<Long, KnowledgeSync>();
		for (KnowledgeSync kp : list) {
			map.put(kp.getCode(), kp);
		}
		return map;
	}

	@Override
	public List<KnowledgeSync> mgetList(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgeSync> list = template.$find(query, KnowledgeSync.class);
		return list;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.KNOWLEDGESYNC;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		template.$dropCollection(KnowledgeSync.class);
		if (CollectionUtils.isEmpty(template.$findAll(KnowledgeSync.class))) {
			List<KnowledgeSync> list = knowledgeSyncRepo.find("$findAllPoint").list();
			template.$insertAll(list);
		}
	}

	@Override
	public long size() {
		return 0;
	}

}
