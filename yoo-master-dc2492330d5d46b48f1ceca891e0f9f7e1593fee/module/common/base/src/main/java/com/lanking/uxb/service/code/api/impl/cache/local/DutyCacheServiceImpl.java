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
import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.DutyService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class DutyCacheServiceImpl extends AbstractBaseDataHandle implements DutyService {

	@Autowired
	@Qualifier("DutyRepo")
	private Repo<Duty, Integer> dutyRepo;

	private List<Duty> allList = null;
	private Map<Integer, Duty> allMap = null;

	@Override
	public Duty get(Integer code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<Duty> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, Duty> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, Duty> map = new HashMap<Integer, Duty>(codes.size());
		for (Integer code : codes) {
			Duty d = allMap.get(code);
			if (d != null) {
				map.put(code, d);
			}
		}
		return map;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.DUTY;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<Duty> tmpAllList = dutyRepo.find("$findAllDuty").list();
		Map<Integer, Duty> tmpAllMap = Maps.newHashMap();
		for (Duty duty : tmpAllList) {
			tmpAllMap.put(duty.getCode(), duty);
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
