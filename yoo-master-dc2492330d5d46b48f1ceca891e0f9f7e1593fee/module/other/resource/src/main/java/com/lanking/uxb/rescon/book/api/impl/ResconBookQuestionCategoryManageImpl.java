package com.lanking.uxb.rescon.book.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;

import httl.util.CollectionUtils;

/**
 * 书本结构习题分类接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2017年11月30日
 */
@Service
@Transactional(readOnly = true)
public class ResconBookQuestionCategoryManageImpl implements ResconBookQuestionCategoryManage {
	@Autowired
	@Qualifier("BookQuestionCategoryRepo")
	Repo<BookQuestionCategory, Long> bookQuestionCategoryRepo;

	@Override
	public BookQuestionCategory get(long questionCategoryId) {
		return bookQuestionCategoryRepo.get(questionCategoryId);
	}

	@Override
	@Transactional
	public BookQuestionCategory createOrUpdate(Long questionCategoryId, long bookVersionId, long bookCatalogId,
			String name) {
		BookQuestionCategory bookQuestionCategory = null;
		if (questionCategoryId != null) {
			bookQuestionCategory = bookQuestionCategoryRepo.get(questionCategoryId);
		}
		if (bookQuestionCategory == null) {
			bookQuestionCategory = new BookQuestionCategory();
		}
		bookQuestionCategory.setBookSectionId(bookCatalogId);
		bookQuestionCategory.setBookVersionId(bookVersionId);
		bookQuestionCategory.setName(name);
		bookQuestionCategoryRepo.save(bookQuestionCategory);
		return bookQuestionCategory;
	}

	@Override
	public List<BookQuestionCategory> queryQuestionCategory(long bookCatalogId) {
		return bookQuestionCategoryRepo.find("$queryQuestionCategory", Params.param("bookCatalogId", bookCatalogId))
				.list();
	}

	@Override
	public Map<Long, List<BookQuestionCategory>> queryQuestionCategory(Collection<Long> bookCatalogIds) {
		if (CollectionUtils.isEmpty(bookCatalogIds)) {
			return Maps.newHashMap();
		}
		Map<Long, List<BookQuestionCategory>> map = new HashMap<Long, List<BookQuestionCategory>>();
		List<BookQuestionCategory> bookQuestionCategorys = bookQuestionCategoryRepo
				.find("$queryQuestionCategorys", Params.param("bookCatalogIds", bookCatalogIds)).list();
		for (BookQuestionCategory bqc : bookQuestionCategorys) {
			List<BookQuestionCategory> bqcs = map.get(bqc.getBookSectionId());
			if (bqcs == null) {
				bqcs = new ArrayList<BookQuestionCategory>();
				map.put(bqc.getBookSectionId(), bqcs);
			}
			bqcs.add(bqc);
		}

		return map;
	}

	@Override
	public int getQuestionCount(long questionCategoryId) {
		return (int) bookQuestionCategoryRepo
				.find("$getQuestionCount", Params.param("questionCategoryId", questionCategoryId)).count();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> mgetQuestionCount(Collection<Long> questionCategoryIds) {
		List<Map> dataMapList = bookQuestionCategoryRepo
				.find("$mgetQuestionCount", Params.param("questionCategoryIds", questionCategoryIds)).list(Map.class);
		Map<Long, Integer> returnDataMap = new HashMap<Long, Integer>(dataMapList.size());
		for (Map data : dataMapList) {
			if (data.get("id") != null) {
				long key = Long.parseLong(data.get("id").toString());
				int value = data.get("qcount") == null ? 0 : Integer.parseInt(data.get("qcount").toString());
				returnDataMap.put(key, value);
			}
		}
		return returnDataMap;
	}

	@Override
	@Transactional
	public void delBookQuestionCategory(long bookCatalogId, Long questionCategoryId) {
		Params param = Params.param("bookCatalogId", bookCatalogId);
		if (questionCategoryId != null) {
			param.put("questionCategoryId", questionCategoryId);
		}
		bookQuestionCategoryRepo.execute("$clearQuestionCategory", param);
		if (questionCategoryId != null) {
			// 仅删除指定的分组
			bookQuestionCategoryRepo.deleteById(questionCategoryId);
		} else {
			// 删除章节目录下的所有分组
			bookQuestionCategoryRepo.execute("$deleteByBookCatalogId", param);
		}
	}

	@Override
	@Transactional
	public void moveToCategory(long bookCatalogId, Long bookQuestionCategoryId, long selectMoveCategoryId,
			List<Long> questionIds) {
		// 更新分类
		Params param = Params.param("bookCatalogId", bookCatalogId);
		param.put("questionCategoryId", selectMoveCategoryId);
		param.put("questionIds", questionIds);
		bookQuestionCategoryRepo.execute("$updateQuestionCategory", param);
	}

	@Transactional
	@Override
	public void delBookQuestionCategoryRelation(long bookCatalogId, Collection<Long> questionIds) {
		Params param = Params.param("bookCatalogId", bookCatalogId);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			param.put("questionIds", questionIds);
		}
		bookQuestionCategoryRepo.execute("$clearQuestionCategory", param);
	}
}
