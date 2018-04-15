package com.lanking.uxb.zycon.book.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookTag;

public interface ZycBookTagService {
	
	/**
	 * 获取初始化选择的书本标签
	 */
	List<BookTag> findTagList();
	
	/**
	 * 获取书本对应的标签
	 * @param bookVersionId
	 * @return
	 */
	Book2Tag findByBookVersionId(Long bookVersionId);
	/**
	 * 新增或修改书本对应的标签 
	 * @param bookVersionId
	 * @param tagId 为空表示新增，不为空表示编辑
	 * @param name
	 */
	void setTag(Long bookVersionId,String name);
}
