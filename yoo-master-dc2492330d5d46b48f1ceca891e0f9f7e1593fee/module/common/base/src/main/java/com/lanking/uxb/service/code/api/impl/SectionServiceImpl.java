package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SectionService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("${common.code.cache}")
public class SectionServiceImpl implements SectionService {
	@Autowired
	@Qualifier("SectionRepo")
	private Repo<Section, Long> sectionRepo;

	@Override
	public List<Section> findByTextbookCode(int textBookCode) {
		return sectionRepo.find("$findByTextbookCode", Params.param("textBookCode", textBookCode)).list();
	}

	@Override
	public List<Section> findByTextbookCode(int textBookCode, Integer level) {
		Params params = Params.param("textBookCode", textBookCode);
		if (level != null) {
			params.put("levelParam", level);
		}
		return sectionRepo.find("$findByTextbookCode", params).list();
	}

	@Override
	public List<Section> findByTextbookCode(List<Integer> textBookCodes, Integer level) {
		Params params = Params.param("textBookCodes", textBookCodes);
		if (level != null) {
			params.put("levelParam", level);
		}
		return sectionRepo.find("$findByTextbookCode", params).list();
	}

	@Override
	public List<Long> findSectionChildren(long code) {
		return sectionRepo.find("$findSectionChildren", Params.param("code", code + "%")).list(Long.class);
	}

	@Override
	public Section get(long code) {
		return sectionRepo.get(code);
	}

	@Override
	public Map<Long, Section> mget(Collection<Long> keys) {
		return sectionRepo.mget(keys);
	}

	@Override
	public List<Section> mgetList(Collection<Long> keys) {
		return sectionRepo.mgetList(keys);
	}

	@Override
	public List<Section> mgetListByTextbookCategory(Collection<Long> keys, int textbookCategoryCode) {
		return sectionRepo.find("$findByTextbookCategoryCode",
				Params.param("codes", keys).put("textbookCategoryCode", textbookCategoryCode)).list();
	}

	@Override
	public List<Section> mgetListByChildId(Long sectionCode) {
		Section sec = sectionRepo.get(sectionCode);
		List<Long> secList = new ArrayList<Long>();
		int start = sec.getTextbookCode().toString().length();
		int end = sec.getCode().toString().length();
		for (int i = start + 2; i <= end; i = i + 2) {
			secList.add(Long.parseLong(sec.getCode().toString().substring(0, i)));
		}
		Map<Long, Section> map = sectionRepo.mget(secList);
		List<Section> returnSections = new ArrayList<Section>(map.size());
		for (Long id : secList) {
			if (null != map.get(id)) {
				returnSections.add(map.get(id));
			}
		}
		return returnSections;
	}

	@Override
	public Section getPSection(long code) {
		return sectionRepo.find("$getPSection", Params.param("code", code)).get();
	}

	@Override
	public Section getNextSection(long code, int textbookCode) {
		return sectionRepo.find("$findNextSection", Params.param("code", code).put("textbookCode", textbookCode)).get();
	}

	@Override
	public Section getFirstLeafSectionByTextbookCode(int textbookCode) {
		return sectionRepo.find("$findNextSection", Params.param("textbookCode", textbookCode)).get();
	}

	@Override
	public List<Long> findNowSectionLeafChildren(long code) {
		return sectionRepo.find("$findChildrenLeaf", Params.param("code", code)).list(Long.class);
	}

	@Override
	public List<Long> findLeafSectionByTextbook(int code) {
		return sectionRepo.find("$findLeafByTextbook", Params.param("code", code)).list(Long.class);
	}

	@Override
	public String getSectionName(Long sectionCode) {
		String sectionName = null;
		// 根据level判断 来拼接获取名称
		if (sectionCode == 0) {
			return sectionName;
		} else {
			Section section = this.get(sectionCode);
			if (section.getLevel() == 1) {
				sectionName = section.getName();
			} else if (section.getLevel() == 2) {
				sectionName = this.getPSection(section.getCode()).getName() + " " + section.getName();
			} else if (section.getLevel() == 3) {
				Long tempCode = this.getPSection(section.getCode()).getCode();
				sectionName = this.getPSection(tempCode).getName() + " " + this.getPSection(section.getCode()).getName()
						+ " " + section.getName();
			}
			return sectionName;
		}
	}

	@Override
	public Map<Long, String> mgetSectionName(List<Long> codes) {
		String sectionName = null;
		Map<Long, String> sectionNameMap = Maps.newHashMap();
		// 去除重合的code
		Set<Long> codeSet = new HashSet<Long>(codes);
		List<Section> sectionList = sectionRepo.find("$findAllSectionName", Params.param("codes", codeSet)).list();
		for (Section d : sectionList) {
			if (d.getLevel() == 1) {
				sectionName = d.getName();
			} else if (d.getLevel() == 2) {
				sectionName = this.getPSection(d.getCode()).getName() + " " + d.getName();
			} else if (d.getLevel() == 3) {
				Long tempCode = this.getPSection(d.getCode()).getCode();
				sectionName = this.getPSection(tempCode).getName() + " " + this.getPSection(d.getCode()).getName() + " "
						+ d.getName();
			} else {

			}
			sectionNameMap.put(d.getCode(), sectionName);
		}

		return sectionNameMap;
	}

	@Override
	public Section findIntegrateSectionCode(long code) {
		return sectionRepo.find("$findntegrateSectionCode", Params.param("code", code)).get();
	}

}
