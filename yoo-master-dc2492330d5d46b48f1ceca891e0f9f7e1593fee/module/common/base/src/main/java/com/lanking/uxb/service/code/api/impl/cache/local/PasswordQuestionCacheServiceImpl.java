package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.PasswordQuestionService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class PasswordQuestionCacheServiceImpl extends AbstractBaseDataHandle implements PasswordQuestionService {

	@Autowired
	@Qualifier("PasswordQuestionRepo")
	private Repo<PasswordQuestion, Long> pqRepo;

	List<PasswordQuestion> allList = null;
	Map<Integer, PasswordQuestion> allMap = null;

	@Override
	public PasswordQuestion get(Integer code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<PasswordQuestion> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public List<PasswordQuestion> mgetList(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		List<PasswordQuestion> list = Lists.newArrayList();
		for (Integer code : codes) {
			PasswordQuestion p = allMap.get(code);
			if (p != null) {
				list.add(p);
			}
		}
		return list;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Integer, PasswordQuestion> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, PasswordQuestion> map = Maps.newHashMap();
		for (Integer code : codes) {
			PasswordQuestion p = allMap.get(code);
			if (p != null) {
				map.put(p.getCode(), p);
			}
		}
		return map;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.PASSWORDQUESTION;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<PasswordQuestion> tmpAllList = pqRepo.getAll();
		Map<Integer, PasswordQuestion> tmpAllMap = Maps.newHashMap();
		for (PasswordQuestion pq : tmpAllList) {
			tmpAllMap.put(pq.getCode(), pq);
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
