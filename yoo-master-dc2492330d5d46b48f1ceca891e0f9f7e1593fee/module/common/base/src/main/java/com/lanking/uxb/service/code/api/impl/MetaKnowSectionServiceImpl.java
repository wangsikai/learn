package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowSection;
import com.lanking.cloud.domain.common.baseData.MetaKnowSectionKey;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class MetaKnowSectionServiceImpl implements MetaKnowSectionService {

	@Autowired
	@Qualifier("MetaKnowSectionRepo")
	private Repo<MetaKnowSection, MetaKnowSectionKey> metaKnowSectionRepo;

	@Override
	public List<Integer> findBySectionCode(long sectionCode) {
		List<Integer> metaknows = Lists.newArrayList();
		List<MetaKnowSection> knowSections = metaKnowSectionRepo
				.find("$findBySectionCode", Params.param("sectionCode", sectionCode)).list();
		if (CollectionUtils.isNotEmpty(knowSections)) {
			for (MetaKnowSection m : knowSections) {
				metaknows.add(m.getMetaCode());
			}
		}
		return metaknows;
	}

	@Override
	public List<Integer> findBySectionCodes(Collection<Long> sectionCodes) {
		List<Integer> metaknows = Lists.newArrayList();
		if (null != sectionCodes) {
			List<MetaKnowSection> knowSections = metaKnowSectionRepo
					.find("$findBySectionCodes", Params.param("sectionCodes", sectionCodes)).list();
			if (CollectionUtils.isNotEmpty(knowSections)) {
				for (MetaKnowSection m : knowSections) {
					metaknows.add(m.getMetaCode());
				}
			}
		}
		return metaknows;
	}

	@Override
	public List<Long> findByMetaknowCodes(Collection<Integer> metaknowCodes) {
		List<Long> sections = Lists.newArrayList();
		if (null != metaknowCodes) {
			List<MetaKnowSection> knowSections = metaKnowSectionRepo
					.find("$findByMetaknowCodes", Params.param("metaknowCodes", metaknowCodes)).list();
			if (CollectionUtils.isNotEmpty(knowSections)) {
				for (MetaKnowSection m : knowSections) {
					sections.add(m.getSectionCode());
				}
			}
		}
		return sections;
	}

	@Override
	public List<Integer> getKnowCodesByCode(long code) {
		return metaKnowSectionRepo.find("$getKnowPointsCodesByCode", Params.param("sectionCodeLike", code + "%"))
				.list(Integer.class);
	}

}
