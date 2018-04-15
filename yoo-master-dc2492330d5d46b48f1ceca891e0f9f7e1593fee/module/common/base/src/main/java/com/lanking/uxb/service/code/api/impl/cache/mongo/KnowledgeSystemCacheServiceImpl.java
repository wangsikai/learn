package com.lanking.uxb.service.code.api.impl.cache.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.impl.cache.local.AbstractBaseDataHandle;
import com.lanking.uxb.service.mongodb.api.LddpMongoTemplate;

@Service
@ConditionalOnExpression("${common.code.cache}")
public class KnowledgeSystemCacheServiceImpl extends AbstractBaseDataHandle implements KnowledgeSystemService {

	@Autowired
	@Qualifier("KnowledgeSystemRepo")
	private Repo<KnowledgeSystem, Long> systemRepo;

	@Autowired
	private LddpMongoTemplate template;

	@Override
	public BaseDataType getType() {
		return BaseDataType.KNOWLEDGESYSTEM;
	}

	@Override
	public KnowledgeSystem get(Long code) {
		return template.$findById(code, KnowledgeSystem.class);
	}

	@Override
	public List<KnowledgeSystem> getBySubjectAndLevel(int level, int subjectCode) {
		Query query = new Query();
		Criteria criteria = Criteria.where("subjectCode").is(subjectCode).and("level").is(level);
		query.addCriteria(criteria);
		query.with(new Sort(Direction.ASC, "code"));
		return template.$find(query, KnowledgeSystem.class);
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		template.$dropCollection(KnowledgeSystem.class);
		if (CollectionUtils.isEmpty(template.$findAll(KnowledgeSystem.class))) {
			List<KnowledgeSystem> list = systemRepo.find("$findAllSystem").list();
			template.$insertAll(list);
		}
	}

	@Override
	public List<KnowledgeSystem> findAllBySubject(Integer subjectCode) {
		Query query = new Query();
		Criteria criteria = Criteria.where("subjectCode").is(subjectCode);
		query.addCriteria(criteria);
		query.with(new Sort(Direction.ASC, "code"));
		List<KnowledgeSystem> list = template.$find(query, KnowledgeSystem.class);
		return list;
	}

	@Override
	public List<KnowledgeSystem> findChildByPcode(Long code) {
		Query query = new Query();
		Criteria criteria = Criteria.where("pcode").is(code);
		query.addCriteria(criteria);
		query.with(new Sort(Direction.ASC, "code"));
		List<KnowledgeSystem> list = template.$find(query, KnowledgeSystem.class);
		return list;
	}

	@Override
	public Map<Long, KnowledgeSystem> mget(Collection<Long> keys) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(keys);
		query.addCriteria(criteria);
		List<KnowledgeSystem> list = template.$find(query, KnowledgeSystem.class);
		Map<Long, KnowledgeSystem> map = new HashMap<Long, KnowledgeSystem>();
		for (KnowledgeSystem ks : list) {
			map.put(ks.getCode(), ks);
		}
		return map;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public List<KnowledgeSystem> findAll() {
		List<KnowledgeSystem> list = template.$findAll(KnowledgeSystem.class);
		return list;
	}

	@Override
	public List<Long> findAllCodeByKPoint(Long code) {
		// 第三层 知识专项
		String third = code.toString().substring(0, code.toString().length() - 2);
		// 第二层 小专题
		String second = third.substring(0, third.length() - 1);
		// 第一层 大专题
		String first = second.substring(0, second.length() - 2);
		List<Long> codes = new ArrayList<Long>();
		codes.add(Long.valueOf(first));
		codes.add(Long.valueOf(second));
		codes.add(Long.valueOf(third));
		codes.add(Long.valueOf(code));
		return codes;
	}

	@Override
	public List<Long> findAllCodeByKPoint(Collection<Long> codes) {
		Set<Long> retCodes = Sets.newHashSet();
		for (Long code : codes) {
			retCodes.addAll(findAllCodeByKPoint(code));
		}
		return Lists.newArrayList(retCodes);
	}

	@Override
	public List<KnowledgeSystem> mgetList(Collection<Long> codes) {
		Query query = new Query();
		Criteria criteria = Criteria.where("code").in(codes);
		query.addCriteria(criteria);
		query.with(new Sort(Direction.ASC, "code"));
		List<KnowledgeSystem> list = template.$find(query, KnowledgeSystem.class);

		return list;
	}

}
