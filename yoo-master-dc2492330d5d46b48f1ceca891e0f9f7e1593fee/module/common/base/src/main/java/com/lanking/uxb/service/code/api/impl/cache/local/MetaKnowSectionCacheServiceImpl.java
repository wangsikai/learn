package com.lanking.uxb.service.code.api.impl.cache.local;

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
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowSection;
import com.lanking.cloud.domain.common.baseData.MetaKnowSectionKey;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class MetaKnowSectionCacheServiceImpl extends AbstractBaseDataHandle implements MetaKnowSectionService {

	@Autowired
	@Qualifier("MetaKnowSectionRepo")
	private Repo<MetaKnowSection, MetaKnowSectionKey> metaKnowSectionRepo;

	private Map<Long, List<Integer>> allSectionMap;
	private Map<Integer, List<Long>> allMetaknowMap;

	@Override
	public List<Integer> findBySectionCode(long sectionCode) {
		if (allSectionMap == null) {
			reload();
		}
		List<Integer> metaknows = allSectionMap.get(sectionCode);
		if (metaknows == null) {
			metaknows = Lists.newArrayList();
		}
		return metaknows;
	}

	@Override
	public List<Integer> findBySectionCodes(Collection<Long> sectionCodes) {
		if (null == sectionCodes) {
			return Lists.newArrayList();
		}
		if (allSectionMap == null) {
			reload();
		}
		List<Integer> metaknows = Lists.newArrayList();
		for (Long sectionCode : sectionCodes) {
			List<Integer> tmps = allSectionMap.get(sectionCode);
			if (null != tmps) {
				metaknows.addAll(tmps);
			}
		}
		return metaknows;
	}

	@Override
	public List<Long> findByMetaknowCodes(Collection<Integer> metaknowCodes) {
		if (null == metaknowCodes) {
			return Lists.newArrayList();
		}
		if (allMetaknowMap == null) {
			reload();
		}
		List<Long> sections = Lists.newArrayList();
		for (Integer metaknowCode : metaknowCodes) {
			List<Long> tmps = allMetaknowMap.get(metaknowCode);
			if (null != tmps) {
				sections.addAll(tmps);
			}
		}
		return sections;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.SECTION_META_KNOWPOINT;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		Map<Long, List<Integer>> tmpAllSectionMap = Maps.newHashMap();
		Map<Integer, List<Long>> tmpAllMetaknowMap = Maps.newHashMap();
		List<MetaKnowSection> allList = metaKnowSectionRepo.find("$findAll").list();
		if (CollectionUtils.isNotEmpty(allList)) {
			for (MetaKnowSection m : allList) {
				List<Integer> one = tmpAllSectionMap.get(m.getSectionCode());
				List<Long> sec = tmpAllMetaknowMap.get(m.getMetaCode());
				if (one == null) {
					one = Lists.newArrayList();
				}
				if (sec == null) {
					sec = Lists.newArrayList();
				}
				one.add(m.getMetaCode());
				sec.add(m.getSectionCode());
				tmpAllSectionMap.put(m.getSectionCode(), one);
				tmpAllMetaknowMap.put(m.getMetaCode(), sec);
			}
		}
		allSectionMap = tmpAllSectionMap;
		allMetaknowMap = tmpAllMetaknowMap;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public List<Integer> getKnowCodesByCode(long code) {
		if (allSectionMap == null) {
			reload();
		}
		List<Integer> knowpointCodes = new ArrayList<Integer>();
		for (Long key : allSectionMap.keySet()) {
			if (key.toString().startsWith(((Long) code).toString())) {
				knowpointCodes.removeAll(allSectionMap.get(key));
				knowpointCodes.addAll(allSectionMap.get(key));
			}
		}
		return knowpointCodes;
	}
}
