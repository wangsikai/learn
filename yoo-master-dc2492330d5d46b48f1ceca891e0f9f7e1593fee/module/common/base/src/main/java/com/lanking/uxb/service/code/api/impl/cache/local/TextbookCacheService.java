package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.ArrayList;
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
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.TextbookService;

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
public class TextbookCacheService extends AbstractBaseDataHandle implements TextbookService {

	@Autowired
	@Qualifier("TextbookRepo")
	Repo<Textbook, Integer> tbRepo;

	private List<Textbook> allList = null;
	private Map<Integer, Textbook> allMap = null;
	private Map<Integer, List<Textbook>> phaseListMap = null;

	@Override
	public Textbook get(int code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<Textbook> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, Textbook> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, Textbook> map = new HashMap<Integer, Textbook>(codes.size());
		for (Integer code : codes) {
			Textbook tb = allMap.get(code);
			if (tb != null) {
				map.put(code, tb);
			}
		}
		return map;
	}

	@Override
	public List<Textbook> mgetList(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		List<Textbook> list = new ArrayList<Textbook>(codes.size());
		for (Integer code : codes) {
			Textbook tb = allMap.get(code);
			if (tb != null) {
				list.add(tb);
			}
		}
		return list;
	}

	@Override
	public List<Textbook> find(int phaseCode, Integer categoryCode, Integer subjectCode) {
		if (phaseListMap == null) {
			reload();
		}
		List<Textbook> list = phaseListMap.get(phaseCode);
		if (list == null) {
			return Collections.EMPTY_LIST;
		}
		List<Textbook> result = Lists.newArrayList();
		if (categoryCode != null || subjectCode != null) {
			for (Textbook textbook : list) {
				if (categoryCode != null && textbook.getCategoryCode().intValue() != categoryCode.intValue()) {
					continue;
				}
				if (subjectCode != null && textbook.getSubjectCode().intValue() != subjectCode.intValue()) {
					continue;
				}
				result.add(textbook);
			}
		}
		return result;
	}

	@Override
	public List<Textbook> find(int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes) {
		if (phaseListMap == null) {
			reload();
		}
		List<Textbook> list = phaseListMap.get(phaseCode);
		if (list == null) {
			return Collections.EMPTY_LIST;
		}
		List<Textbook> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(categoryCodes)) {
			categoryCodes = new ArrayList<Integer>(1);
			categoryCodes.add(-1);
		}
		for (Textbook textbook : list) {
			if (!categoryCodes.contains(textbook.getCategoryCode().intValue())) {
				continue;
			}
			if (subjectCode != null && textbook.getSubjectCode().intValue() != subjectCode.intValue()) {
				continue;
			}
			result.add(textbook);
		}
		return result;
	}

	@Override
	public List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes) {
		if (phaseListMap == null) {
			reload();
		}
		List<Textbook> list = phaseListMap.get(phaseCode);
		if (list == null) {
			return Collections.EMPTY_LIST;
		}
		List<Textbook> result = Lists.newArrayList();
		for (Textbook textbook : list) {
			if (!categoryCodes.contains(textbook.getCategoryCode().intValue())) {
				continue;
			}
			if (textbook.getSubjectCode().intValue() != subjectCode.intValue()) {
				continue;
			}
			if (product == Product.YOOMATH && textbook.getYoomathStatus() != Status.ENABLED) {
				continue;
			}
			result.add(textbook);
		}
		return result;
	}

	@Override
	public List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Integer categoryCode) {
		if (phaseListMap == null) {
			reload();
		}
		List<Textbook> list = phaseListMap.get(phaseCode);
		if (list == null) {
			return Collections.EMPTY_LIST;
		}
		List<Textbook> result = Lists.newArrayList();
		for (Textbook textbook : list) {
			if (categoryCode.intValue() != textbook.getCategoryCode().intValue()) {
				continue;
			}
			if (textbook.getSubjectCode().intValue() != subjectCode.intValue()) {
				continue;
			}
			if (product == Product.YOOMATH && textbook.getYoomathStatus() != Status.ENABLED) {
				continue;
			}
			result.add(textbook);
		}
		return result;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.TEXTBOOK;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<Textbook> tmpAllList = tbRepo.find("$findAll").list();
		Map<Integer, Textbook> tmpAllMap = Maps.newHashMap();
		Map<Integer, List<Textbook>> tmpPhaseListMap = Maps.newHashMap();
		for (Textbook tb : tmpAllList) {
			tmpAllMap.put(tb.getCode(), tb);
			List<Textbook> list = tmpPhaseListMap.get(tb.getPhaseCode());
			if (list == null) {
				list = Lists.newArrayList();
			}
			list.add(tb);
			tmpPhaseListMap.put(tb.getPhaseCode(), list);
		}
		allList = tmpAllList;
		allMap = tmpAllMap;
		phaseListMap = tmpPhaseListMap;
	}

	@Override
	public long size() {
		Long allListSize = getObjectDeepSize(allList);
		Long allMapSize = getObjectDeepSize(allMap);
		Long allPhaseMapSize = getObjectDeepSize(phaseListMap);

		return allListSize + allMapSize + allPhaseMapSize;
	}

}
