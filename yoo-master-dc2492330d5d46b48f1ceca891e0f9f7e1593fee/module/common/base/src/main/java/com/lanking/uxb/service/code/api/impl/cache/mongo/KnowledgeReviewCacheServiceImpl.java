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
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.KnowledgeReviewService;
import com.lanking.uxb.service.code.api.impl.cache.local.AbstractBaseDataHandle;
import com.lanking.uxb.service.mongodb.api.LddpMongoTemplate;

@Service
@ConditionalOnExpression("${common.code.cache}")
public class KnowledgeReviewCacheServiceImpl extends AbstractBaseDataHandle implements KnowledgeReviewService {
	@Autowired
	@Qualifier("KnowledgeReviewRepo")
	private Repo<KnowledgeReview, Long> knowledgeReviewRepo;

	@Autowired
	private LddpMongoTemplate template;

	@Override
	public Map<Long, KnowledgeReview> mget(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgeReview> list = template.$find(query, KnowledgeReview.class);
		Map<Long, KnowledgeReview> map = new HashMap<Long, KnowledgeReview>();
		for (KnowledgeReview kp : list) {
			map.put(kp.getCode(), kp);
		}
		return map;
	}

	@Override
	public List<KnowledgeReview> mgetList(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		List<KnowledgeReview> list = template.$find(query, KnowledgeReview.class);
		return list;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.KNOWLEDGEREVIEW;
	}

	@Override
	@Transactional(readOnly = true)
	public void reload() {
		template.$dropCollection(KnowledgeReview.class);
		if (CollectionUtils.isEmpty(template.$findAll(KnowledgeReview.class))) {
			List<KnowledgeReview> list = knowledgeReviewRepo.find("$findAllPoint").list();
			template.$insertAll(list);
		}
	}

	@Override
	public long size() {
		return 0;
	}

}
