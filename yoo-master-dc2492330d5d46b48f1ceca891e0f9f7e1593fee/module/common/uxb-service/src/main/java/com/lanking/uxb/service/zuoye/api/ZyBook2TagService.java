package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.Book2Tag;

public interface ZyBook2TagService {
	
	/**
	 * 通过书本版本id获取对应的书本标签
	 * @param versionId
	 * @return
	 */
	Book2Tag getByBookVersionId(Long versionId);
	
	/**
	 * 通过书本版本id集合批量获取
	 * @param versionIds
	 * @return
	 */
	Map<Long,Book2Tag> mgetByBookVersionIds(Collection<Long> versionIds);
}
