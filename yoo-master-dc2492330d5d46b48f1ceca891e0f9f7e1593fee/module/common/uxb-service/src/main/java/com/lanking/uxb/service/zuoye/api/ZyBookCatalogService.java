package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookCatalog;

public interface ZyBookCatalogService {

	/**
	 * 获得书本章节.
	 * 
	 * @param id
	 *            章节ID.
	 * @return
	 */
	BookCatalog get(long id);

	/**
	 * 批量获得书本章节.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, BookCatalog> mget(Collection<Long> ids);

	/**
	 * 获取BookCatalog
	 * 
	 * @param bookVersionId
	 *            关联书本版本ID
	 * @param textbookCode
	 *            教材代码
	 * @param sectionCode
	 *            章节代码
	 * @return bookCatalog
	 */
	List<BookCatalog> listBookCatalog(long bookVersionId, long textbookCode, long sectionCode);
}
