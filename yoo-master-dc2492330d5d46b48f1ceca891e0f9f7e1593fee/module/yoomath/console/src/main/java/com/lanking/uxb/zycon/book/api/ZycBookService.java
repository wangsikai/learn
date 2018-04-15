package com.lanking.uxb.zycon.book.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.Book;

public interface ZycBookService {
	/**
	 * 获取书本
	 * 
	 * @param id
	 * @return
	 */
	Book get(Long id);

	/**
	 * 批量获取书本
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, Book> mget(Collection<Long> ids);

}
