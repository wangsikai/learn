package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;

/**
 * 新知识体系
 * 
 * @author wangsenhao
 *
 */
@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class KnowledgeSystemServiceImpl implements KnowledgeSystemService {

	@Autowired
	@Qualifier("KnowledgeSystemRepo")
	private Repo<KnowledgeSystem, Long> systemRepo;

	@Override
	public KnowledgeSystem get(Long code) {
		return systemRepo.get(code);
	}

	@Override
	public List<KnowledgeSystem> getBySubjectAndLevel(int level, int subjectCode) {
		return systemRepo.find("$findBySubjectAndLevel", Params.param("level", level).put("subjectCode", subjectCode))
				.list();
	}

	@Override
	public List<KnowledgeSystem> findAll() {
		return systemRepo.find("$findAllSystem").list();
	}

	@Override
	public List<KnowledgeSystem> findAllBySubject(Integer subjectCode) {
		return systemRepo.find("$findAllBySubject", Params.param("subjectCode", subjectCode)).list();
	}

	@Override
	public List<KnowledgeSystem> findChildByPcode(Long code) {
		return systemRepo.find("$findChildByPcode", Params.param("code", code)).list();
	}

	@Override
	public Map<Long, KnowledgeSystem> mget(Collection<Long> keys) {
		return systemRepo.mget(keys);
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
		List<Long> retCodes = Lists.newArrayList();
		for (Long code : codes) {
			retCodes.addAll(findAllCodeByKPoint(code));
		}
		return retCodes;
	}

	@Override
	public List<KnowledgeSystem> mgetList(Collection<Long> codes) {
		return systemRepo.mgetList(codes);
	}

}
