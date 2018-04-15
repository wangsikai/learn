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
import com.lanking.cloud.domain.yoo.honor.point.PointAction;
import com.lanking.cloud.domain.yoo.honor.point.PointRule;
import com.lanking.uxb.service.honor.api.PointRuleService;

@Transactional(readOnly = true)
@Service
public class PointRuleServiceImpl implements PointRuleService {
	@Autowired
	@Qualifier("PointRuleRepo")
	private Repo<PointRule, Long> pointRuleRepo;

	static Map<PointAction, PointRule> allByAction = null;
	static Map<Integer, PointRule> allById = null;

	void init() {
		if (allById == null) {
			List<PointRule> all = pointRuleRepo.find("$getAll").list();
			allById = Maps.newHashMap();
			allByAction = Maps.newHashMap();
			for (PointRule pointRule : all) {
				allById.put(pointRule.getCode(), pointRule);
				allByAction.put(pointRule.getAction(), pointRule);
			}
		}
	}

	@Override
	public PointRule getByAction(PointAction action) {
		init();
		return allByAction.get(action);
	}

	@Override
	public PointRule getByCode(int code) {
		init();
		return allById.get(code);
	}

	@Override
	public Map<Integer, PointRule> mgetPointRule(Collection<Integer> codes) {
		init();
		Map<Integer, PointRule> map = Maps.newHashMap();
		for (int code : codes) {
			PointRule pointRule = allById.get(code);
			if (pointRule != null) {
				map.put(code, pointRule);
			}
		}
		return map;
	}

}
