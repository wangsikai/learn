package com.lanking.uxb.service.search.api;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.lanking.cloud.sdk.bean.IndexTypeable;

public interface IndexConfigService {

	void mapping(IndexTypeable type, XContentBuilder source) throws IOException;

	void mapping(IndexTypeable type, String source) throws IOException;

	/**
	 * 通过indexName删除
	 * 
	 * @param indexName
	 */
	void deleteByIndexName(String indexName);

	/**
	 * 通过indexName创建
	 * 
	 * @param indexName
	 */
	void createByIndexName(String indexName);

	/**
	 * 判断当前indexName是否存在
	 * 
	 * @param indexName
	 * @return
	 */
	boolean isExistByIndexName(String indexName);
}
