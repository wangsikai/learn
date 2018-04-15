package com.lanking.uxb.service.search.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.sdk.bean.IndexTypeable;

/**
 * 操作索引的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年4月1日
 */
public interface IndexService {

	// 异步
	void add(IndexTypeable type, Long id);

	void add(IndexTypeable type, Collection<Long> ids);

	void update(IndexTypeable type, Long id);

	void update(IndexTypeable type, Long id, Map<String, Object> fieldValues);

	void update(IndexTypeable type, Collection<Long> ids);

	void delete(IndexTypeable type, Long id);

	void delete(IndexTypeable type, Collection<Long> ids);

	void reindex(IndexTypeable type);

	void deleteReindex(IndexTypeable type);

	// 同步
	void syncAdd(IndexTypeable type, Long id);

	void syncAdd(IndexTypeable type, Collection<Long> ids);

	void syncUpdate(IndexTypeable type, Long id);

	void syncUpdate(IndexTypeable type, Long id, Map<String, Object> fieldValues);

	void syncUpdate(IndexTypeable type, Collection<Long> ids);

	void syncDelete(IndexTypeable type, Long id);

	void syncDelete(IndexTypeable type, Collection<Long> ids);

	void syncReindex(IndexTypeable type);

	void syncDeleteReindex(IndexTypeable type);
}
