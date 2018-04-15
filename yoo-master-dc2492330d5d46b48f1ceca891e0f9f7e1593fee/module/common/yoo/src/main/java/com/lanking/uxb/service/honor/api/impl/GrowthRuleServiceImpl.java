package com.lanking.uxb.service.honor.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo ;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;
import com.lanking.uxb.service.honor.api.GrowthRuleService;

@Transactional(readOnly = true)
@Service
public class GrowthRuleServiceImpl implements GrowthRuleService {
	@Autowired
	@Qualifier("GrowthRuleRepo")
	private Repo<GrowthRule, Long> GrowthRuleRepo;

	static Map<GrowthAction, GrowthRule> allByAction = null;
	static Map<Integer, GrowthRule> allById = null;

	void init() {
		if (allById == null) {
			List<GrowthRule> all = GrowthRuleRepo.find("$getAll").list();
			allById = Maps.newHashMap();
			allByAction = Maps.newHashMap();
			for (GrowthRule GrowthRule : all) {
				allById.put(GrowthRule.getCode(), GrowthRule);
				allByAction.put(GrowthRule.getAction(), GrowthRule);
			}
		}
	}

	@Override
	public GrowthRule getByAction(GrowthAction action) {
		init();
		return allByAction.get(action);
	}

	@Override
	public GrowthRule getByCode(int code) {
		init();
		return allById.get(code);
	}

	@Override
	public Map<Integer, GrowthRule> mgetGrowthRule(Collection<Integer> codes) {
		init();
		Map<Integer, GrowthRule> map = Maps.newHashMap();
		for (int code : codes) {
			GrowthRule GrowthRule = allById.get(code);
			if (GrowthRule != null) {
				map.put(code, GrowthRule);
			}
		}
		return map;
	}

}
