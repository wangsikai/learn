package com.lanking.uxb.service.search.api.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.search.api.IndexBuildService;

@MappedSuperclass
public class IndexBuildServiceImpl implements IndexBuildService {

	private Logger logger = LoggerFactory.getLogger(IndexBuildServiceImpl.class);

	@Value("${elasticsearch.index-name}")
	private String indexName;
	@Autowired
	private Client elasticsearchClient;

	private void addIndexType(Document document) {
		JSONObject json = JSONObject.parseObject(document.getValue());
		json.put("indexType", document.getType());
		document.setValue(json.toString());
	}

	@Override
	public void putDocument(Document document) {
		addIndexType(document);
		IndexResponse response = elasticsearchClient.prepareIndex(indexName, document.getType(), document.getId())
				.setSource(document.getValue(), XContentType.JSON).execute().actionGet();
		logger.info("创建索引成功,type = {},id = {}", response.getType(), response.getId());
	}

	@Override
	public void putDocuments(List<Document> documents) {
		if (CollectionUtils.isNotEmpty(documents)) {
			BulkRequestBuilder bulkBuilder = elasticsearchClient.prepareBulk();
			for (Document document : documents) {
				addIndexType(document);
				IndexRequestBuilder indexBuilder = elasticsearchClient
						.prepareIndex(indexName, document.getType(), document.getId())
						.setSource(document.getValue(), XContentType.JSON);
				bulkBuilder.add(indexBuilder);
			}
			BulkResponse response = bulkBuilder.execute().actionGet();
			if (response.hasFailures()) {
				for (BulkItemResponse item : response.getItems()) {
					if (item.isFailed()) {
						logger.error(item.getFailure().getMessage());
					}
				}
			} else {
				logger.info("创建文档成功:{}", documents.size());
			}
		}
	}

	@Override
	public void updateDocument(Document document) {
		addIndexType(document);
		UpdateResponse response = elasticsearchClient.prepareUpdate(indexName, document.getType(), document.getId())
				.setDoc(document.getValue(), XContentType.JSON).execute().actionGet();
		logger.info("创建索引成功 type = {},id = {}", response.getType(), response.getId());
	}

	@Override
	public void updateDocuments(List<Document> documents) {
		if (CollectionUtils.isNotEmpty(documents)) {
			BulkRequestBuilder bulkBuilder = elasticsearchClient.prepareBulk();
			for (Document document : documents) {
				addIndexType(document);
				UpdateRequestBuilder updateBuilder = elasticsearchClient
						.prepareUpdate(indexName, document.getType(), document.getId()).setUpsert(document.getValue());
				bulkBuilder.add(updateBuilder);
			}
			BulkResponse response = bulkBuilder.execute().actionGet();
			if (response.hasFailures()) {
				for (BulkItemResponse item : response.getItems()) {
					if (item.isFailed()) {
						logger.error(item.getFailure().getMessage());
					}
				}
			} else {
				logger.info("更新文档成功:{}", documents.size());
			}
		}
	}

	@Override
	public void updateFieldValues(IndexTypeable type, long id, Map<String, Object> fieldValues) {
		Document document = new Document(type.toString(), String.valueOf(id), JSON.toJSONString(fieldValues));
		updateDocument(document);
	}

	@Override
	public void deleteDocument(IndexTypeable type, String id) {
		DeleteResponse response = elasticsearchClient.prepareDelete(indexName, type.toString(), id).execute()
				.actionGet();
		logger.info("删除索引成功：{}", response.getId());
	}

	@Override
	public void deleteByType(IndexTypeable type) {
		elasticsearchClient.prepareDelete().setIndex(indexName).setType(type.toString()).execute();
	}

	@Override
	public void deleteByIndex() {
		elasticsearchClient.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
	}

	@Override
	public void deleteByQuery(IndexTypeable type, QueryBuilder query) {
		if (query == null) {
			query = QueryBuilders.queryStringQuery("indexType:\"" + type.toString() + "\"");
		}
		BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(elasticsearchClient)
				.filter(query).source(indexName).get();
		response.getDeleted();
	}
}
