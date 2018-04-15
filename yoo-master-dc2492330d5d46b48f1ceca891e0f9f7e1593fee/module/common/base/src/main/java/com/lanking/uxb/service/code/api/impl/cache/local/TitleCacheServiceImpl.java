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
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.TitleService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class TitleCacheServiceImpl extends AbstractBaseDataHandle implements TitleService {

	@Autowired
	@Qualifier("TitleRepo")
	private Repo<Title, Integer> titleRepo;

	private List<Title> allList = null;
	private Map<Integer, Title> allMap = null;

	@Override
	public Title getTitle(Integer code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<Title> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, Title> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, Title> map = new HashMap<Integer, Title>(codes.size());
		for (Integer code : codes) {
			Title title = allMap.get(code);
			if (title != null) {
				map.put(code, title);
			}
		}
		return map;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.TITLE;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<Title> tmpAllList = titleRepo.find("$findAllTitle").list();
		Map<Integer, Title> tmpAllMap = Maps.newHashMap();
		for (Title title : tmpAllList) {
			tmpAllMap.put(title.getCode(), title);
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
