package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.ArrayList;
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
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.TextbookCategoryService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Service
@ConditionalOnExpression("${common.code.cache}")
public class TextbookCategoryCacheService extends AbstractBaseDataHandle implements TextbookCategoryService {

	@Autowired
	@Qualifier("TextbookCategoryRepo")
	Repo<TextbookCategory, Integer> tbCategoryRepo;

	private List<TextbookCategory> allList = null;
	private Map<Integer, TextbookCategory> allMap = null;

	@Override
	public TextbookCategory get(Integer code) {
		if (allMap == null) {
			reload();
		}
		return allMap.get(code);
	}

	@Override
	public List<TextbookCategory> getAll() {
		if (allList == null) {
			reload();
		}
		return allList;
	}

	@Override
	public Map<Integer, TextbookCategory> mget(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		Map<Integer, TextbookCategory> map = new HashMap<Integer, TextbookCategory>(codes.size());
		for (Integer code : codes) {
			TextbookCategory tbc = allMap.get(code);
			if (tbc != null) {
				map.put(code, tbc);
			}
		}
		return map;
	}

	@Override
	public List<TextbookCategory> mgetList(Collection<Integer> codes) {
		if (allMap == null) {
			reload();
		}
		List<TextbookCategory> list = new ArrayList<TextbookCategory>(codes.size());
		for (Integer code : codes) {
			TextbookCategory tbc = allMap.get(code);
			if (tbc != null) {
				list.add(tbc);
			}
		}
		return list;
	}

	@Override
	public List<TextbookCategory> find(Product product, Integer codePhase) {
		List<TextbookCategory> list = new ArrayList<TextbookCategory>(allList.size());
		for (TextbookCategory tbc : allList) {
			if (product == Product.YOOMATH && tbc.getYoomathStatus() == Status.ENABLED && tbc.support(codePhase)) {
				list.add(tbc);
			}
		}
		return list;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.TEXTBOOKCATEGORY;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		List<TextbookCategory> tmpAllList = tbCategoryRepo.find("$findAllTextbookCategory").list();
		Map<Integer, TextbookCategory> tmpAllMap = Maps.newHashMap();
		for (TextbookCategory tbc : tmpAllList) {
			tmpAllMap.put(tbc.getCode(), tbc);
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
