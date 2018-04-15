package com.lanking.uxb.service.search.api;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;

import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.sdk.bean.IndexTypeable;

/**
 * 构建索引的相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月12日
 */
public interface IndexBuildService {

	void putDocument(Document document);

	void putDocuments(List<Document> documents);

	void updateDocument(Document document);

	void updateDocuments(List<Document> documents);

	void updateFieldValues(IndexTypeable type, long id, Map<String, Object> fieldValues);

	void deleteDocument(IndexTypeable type, String id);

	void deleteByType(IndexTypeable type);

	void deleteByIndex();

	void deleteByQuery(IndexTypeable type, QueryBuilder query);

}
