package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.PhaseService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class PhaseCacheServiceImpl extends AbstractBaseDataHandle implements PhaseService {

	@Autowired
	@Qualifier("PhaseRepo")
	private Repo<Phase, Long> phaseRepo;

	private List<Phase> allList = null;
	private Map<Integer, Phase> allMap = null;

	@Override
	public Phase get(int code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);

	}

	@Override
	public List<Phase> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, Phase> mgetAll() {
		if (allMap == null) {
			reload();
		}
		return allMap;
	}

	@Override
	public Map<Integer, Phase> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, Phase> map = new HashMap<Integer, Phase>();
		for (Integer code : codes) {
			Phase p = allMap.get(code);
			if (p != null) {
				map.put(code, p);
			}
		}
		return map;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.PHASE;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<Phase> tmpAllList = phaseRepo.find("$findAllPhase").list();
		Map<Integer, Phase> tmpAllMap = Maps.newHashMap();
		for (Phase p : tmpAllList) {
			tmpAllMap.put(p.getCode(), p);
		}
		allList = tmpAllList;
		allMap = tmpAllMap;
	}

	@Override
	public long size() {
		Long allListSize = getObjectDeepSize(allList);
		Long allMapSize = getObjectDeepSize(allMap);
		return allListSize + allMapSize;
	}

}
