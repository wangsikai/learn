package com.lanking.uxb.service.search.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.sdk.bean.IndexTypeable;

public interface IndexHandle extends IndexBuildService {

	public static final int PAGE_SIZE = 200;

	boolean accept(IndexTypeable type);

	IndexTypeable getType();

	String getDescription();

	Document get(Long bizId);

	List<Document> mget(Collection<Long> ids);

	void add(Long id);

	void add(Collection<Long> ids);

	void update(Long id);

	void update(Long id, Map<String, Object> fieldValues);

	void update(Collection<Long> ids);

	void delete(Long id);

	void delete(Collection<Long> ids);

	void reMapping() throws IOException;

	void reindex();

	void deleteReindex();

	// 当前索引的数量
	long indexCount();

	// 实际数据的数量
	long dataCount();

	/**
	 * 继续重建(在原来基础上重建或指定页数范围重建)
	 * 
	 * @param startPage
	 *            为空默认为继续重建
	 * @param endPage
	 *            为空指定页开始重建
	 */
	void continueReindex(Integer startPage, Integer endPage);

}
