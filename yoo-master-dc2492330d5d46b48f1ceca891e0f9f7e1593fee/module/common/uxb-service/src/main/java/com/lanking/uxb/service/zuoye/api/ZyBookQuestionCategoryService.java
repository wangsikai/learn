package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;

public interface ZyBookQuestionCategoryService {
	
	/**
	 * 通过目录id获取对应分类
	 * @param bookCatalogId
	 * @return
	 */
	List<BookQuestionCategory> findListByCatalogId(Long bookCatalogId);
	/**
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long,BookQuestionCategory> mgetByCatalogIds(Collection<Long> ids);
}
