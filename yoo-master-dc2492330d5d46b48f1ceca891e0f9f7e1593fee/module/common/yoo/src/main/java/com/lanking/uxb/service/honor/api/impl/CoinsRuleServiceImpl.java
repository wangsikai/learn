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
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.uxb.service.honor.api.CoinsRuleService;

@Transactional(readOnly = true)
@Service
public class CoinsRuleServiceImpl implements CoinsRuleService {
	@Autowired
	@Qualifier("CoinsRuleRepo")
	private Repo<CoinsRule, Long> coinsRuleRepo;

	static Map<CoinsAction, CoinsRule> allByAction = null;
	static Map<Integer, CoinsRule> allById = null;

	void init() {
		if (allById == null) {
			List<CoinsRule> all = coinsRuleRepo.find("$getAll").list();
			allById = Maps.newHashMap();
			allByAction = Maps.newHashMap();
			for (CoinsRule CoinsRule : all) {
				allById.put(CoinsRule.getCode(), CoinsRule);
				allByAction.put(CoinsRule.getAction(), CoinsRule);
			}
		}
	}

	@Override
	public CoinsRule getByAction(CoinsAction action) {
		init();
		return allByAction.get(action);
	}

	@Override
	public CoinsRule getByCode(int code) {
		init();
		return allById.get(code);
	}

	@Override
	public Map<Integer, CoinsRule> mgetCoinsRule(Collection<Integer> codes) {
		init();
		Map<Integer, CoinsRule> map = Maps.newHashMap();
		for (int code : codes) {
			CoinsRule CoinsRule = allById.get(code);
			if (CoinsRule != null) {
				map.put(code, CoinsRule);
			}
		}
		return map;
	}
}
