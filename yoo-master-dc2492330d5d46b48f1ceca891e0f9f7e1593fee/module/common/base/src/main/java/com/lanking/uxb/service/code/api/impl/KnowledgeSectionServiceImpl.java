package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSection;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.SectionService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class KnowledgeSectionServiceImpl implements KnowledgeSectionService {
	@Autowired
	@Qualifier("KnowledgeSectionRepo")
	private Repo<KnowledgeSection, Long> repo;
	@Autowired
	private SectionService sectionService;

	@Override
	public List<Long> getBySection(long code) {
		return repo.find("$findByCode", Params.param("code", code + "%")).list(Long.class);
	}

	@Override
	public List<Long> findBySectionCode(long code) {
		List<KnowledgeSection> knowledgeSections = repo
				.find("$findBySectionCodes", Params.param("codes", Lists.newArrayList(code))).list();
		List<Long> knowledgeCodes = new ArrayList<Long>(knowledgeSections.size());
		if (CollectionUtils.isNotEmpty(knowledgeSections)) {
			for (KnowledgeSection m : knowledgeSections) {
				knowledgeCodes.add(m.getKnowledgeCode());
			}
		}
		return knowledgeCodes;
	}

	@Override
	public Map<Long, List<Long>> mGetKnowledgeSectionMap(Collection<Long> codes) {
		List<KnowledgeSection> knowledgeSections = repo.find("$findBySectionCodes", Params.param("codes", codes))
				.list();
		Map<Long, List<Long>> map = new HashMap<Long, List<Long>>(codes.size());
		if (CollectionUtils.isNotEmpty(knowledgeSections)) {
			for (Long code : codes) {
				List<Long> knowledgeCodes = Lists.newArrayList();
				for (KnowledgeSection ks : knowledgeSections) {
					if (code.longValue() == ks.getSectionCode()) {
						knowledgeCodes.add(ks.getKnowledgeCode());
					}
				}
				map.put(code, knowledgeCodes);
			}
		}
		return map;
	}

	@Override
	public List<Long> getByTextbook(int code) {
		return repo.find("$findByCode", Params.param("code", code + "%")).list(Long.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> queryTextbookByKnowledge(Collection<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}
		return repo.find("$findTextbookByKnowledge", Params.param("codes", codes)).list(Integer.class);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Map<Long, Set<Integer>> queryTextbookByKnowledgeRelation(Collection<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Maps.newHashMap();
		}
		List<Map> list = repo.find("$findTextbookByKnowledgeRelation", Params.param("codes", codes)).list(Map.class);
		Map<Long, Set<Integer>> results = new HashMap<Long, Set<Integer>>(list.size());
		for (Map map : list) {
			long knowledgeCode = Long.parseLong(map.get("knowledge_code").toString());
			int textbookCode = Integer.parseInt(map.get("textbook_code").toString());
			Set<Integer> textbookCodes = results.get(knowledgeCode);
			if (textbookCodes == null) {
				textbookCodes = new HashSet<Integer>();
				results.put(knowledgeCode, textbookCodes);
			}
			textbookCodes.add(textbookCode);
		}
		return results;
	}

	@SuppressWarnings("static-access")
	@Override
	public List<Section> findSectionsByMetaknowCodes(Collection<Long> KnowledgeCodes, Integer textbookCategoryCode) {
		Params params = Params.param("knowledgeCodes", KnowledgeCodes);
		if (null != textbookCategoryCode) {
			params.param("textbookCategoryCode", textbookCategoryCode);
		}
		return repo.find("$findSectionsByMetaknowCodes", params).list(Section.class);
	}

	@Override
	public List<Section> findSections(Collection<Long> KnowledgeCodes, Integer textbookCategoryCode) {
		Params params = Params.param("knowledgeCodes", KnowledgeCodes);
		if (null != textbookCategoryCode) {
			params.put("textbookCategoryCode", textbookCategoryCode);
		}
		return repo.find("$findSectionsByMetaknowCodes", params).list(Section.class);
	}

	@Override
	public Map<Long, Set<Section>> findSectionRelationByKnowledgeCodes(Collection<Long> knowledgeCodes) {
		List<KnowledgeSection> knowledgeSections = repo
				.find("$findSectionRelationByKnowledgeCodes", Params.param("knowledgeCodes", knowledgeCodes)).list();
		Map<Long, Set<Section>> map = new HashMap<Long, Set<Section>>();

		Set<Long> sectionCodes = new HashSet<Long>();
		for (KnowledgeSection ks : knowledgeSections) {
			sectionCodes.add(ks.getSectionCode());
		}
		Map<Long, Section> sectionMap = sectionService.mget(sectionCodes);
		for (KnowledgeSection ks : knowledgeSections) {
			Set<Section> sections = map.get(ks.getKnowledgeCode());
			if (sections == null) {
				sections = new HashSet<Section>();
				map.put(ks.getKnowledgeCode(), sections);
			}
			if (null != sectionMap.get(ks.getSectionCode())) {
				sections.add(sectionMap.get(ks.getSectionCode()));
			}
		}
		return map;
	}
}
