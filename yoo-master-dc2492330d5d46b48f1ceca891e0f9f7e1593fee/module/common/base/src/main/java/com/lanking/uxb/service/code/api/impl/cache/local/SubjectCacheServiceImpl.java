package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.SubjectService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@SuppressWarnings("unchecked")
@Service
@ConditionalOnExpression("${common.code.cache}")
public class SubjectCacheServiceImpl extends AbstractBaseDataHandle implements SubjectService {

	@Autowired
	@Qualifier("SubjectRepo")
	private Repo<Subject, Integer> subjectRepo;

	private List<Subject> allList = null;
	private Map<Integer, Subject> allMap = null;
	private Map<Integer, List<Subject>> phaseListMap = null;

	@Override
	public Subject get(Integer code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<Subject> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, Subject> mgetAll() {
		if (allMap == null) {
			reload();
		}
		return allMap;
	}

	@Override
	public Map<Integer, Subject> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, Subject> map = new HashMap<Integer, Subject>(codes.size());
		for (Integer code : codes) {
			Subject s = allMap.get(code);
			if (s != null) {
				map.put(code, s);
			}
		}
		return map;

	}

	@Override
	public List<Subject> findByPhaseCode(Integer phaseCode) {
		if (phaseListMap == null) {
			reload();
		}
		List<Subject> subjectList = phaseListMap.get(phaseCode);
		if (subjectList == null) {
			return Collections.EMPTY_LIST;
		}
		return subjectList;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.SUBJECT;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<Subject> tmpAllList = subjectRepo.find("$findAllSubject").list();
		Map<Integer, Subject> tmpAllMap = Maps.newHashMap();
		Map<Integer, List<Subject>> tmpPhaseListMap = Maps.newHashMap();
		for (Subject s : tmpAllList) {
			tmpAllMap.put(s.getCode(), s);
			List<Subject> list = tmpPhaseListMap.get(s.getPhaseCode());
			if (list == null) {
				list = Lists.newArrayList();
			}
			list.add(s);
			tmpPhaseListMap.put(s.getPhaseCode(), list);
		}
		allList = tmpAllList;
		allMap = tmpAllMap;
		phaseListMap = tmpPhaseListMap;
	}

	@Override
	public long size() {
		Long allListSize = getObjectDeepSize(allList);
		Long allMapSize = getObjectDeepSize(allMap);
		Long phaseListMapSize = getObjectDeepSize(phaseListMap);

		return allListSize + allMapSize + phaseListMapSize;
	}

}
