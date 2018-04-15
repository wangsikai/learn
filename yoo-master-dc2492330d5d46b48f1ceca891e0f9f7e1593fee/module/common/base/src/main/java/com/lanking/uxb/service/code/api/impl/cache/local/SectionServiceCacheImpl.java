package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.SectionService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class SectionServiceCacheImpl extends AbstractBaseDataHandle implements SectionService {

	@Autowired
	@Qualifier("SectionRepo")
	private Repo<Section, Long> sectionRepo;

	@Override
	public List<Section> findByTextbookCode(int textBookCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Section> findByTextbookCode(int textBookCode, Integer level) {
		return null;
	}

	@Override
	public List<Section> findByTextbookCode(List<Integer> textBookCodes, Integer level) {
		return null;
	}

	@Override
	public List<Long> findSectionChildren(long code) {
		return null;
	}

	@Override
	public Section get(long code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Section> mget(Collection<Long> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Section> mgetList(Collection<Long> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Section> mgetListByTextbookCategory(Collection<Long> keys, int textbookCategoryCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Section> mgetListByChildId(Long sectionCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSectionName(Long sectionCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, String> mgetSectionName(List<Long> codes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Section getPSection(long code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Section getNextSection(long code, int textbookCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Section getFirstLeafSectionByTextbookCode(int textbookCode) {
		return null;
	}

	@Override
	public List<Long> findNowSectionLeafChildren(long code) {
		return null;
	}

	@Override
	public List<Long> findLeafSectionByTextbook(int code) {
		return null;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.SECTION;
	}

	@Override
	public void reload() {

	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public Section findIntegrateSectionCode(long code) {
		return null;
	}
}
