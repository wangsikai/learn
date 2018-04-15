package com.lanking.uxb.service.search.api.impl;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.uxb.service.search.api.IndexConfigService;

@Service("indexConfigService")
@Transactional(readOnly = true)
public class IndexConfigServiceImpl implements IndexConfigService {

	private Logger logger = LoggerFactory.getLogger(IndexConfigServiceImpl.class);

	@Autowired
	private Client elasticsearchClient;

	@Value("${elasticsearch.index-name}")
	private String indexName;

	@Override
	public void mapping(IndexTypeable type, XContentBuilder source) throws IOException {
		mapping(type, source.string());
	}

	@Override
	public void mapping(IndexTypeable type, String source) throws IOException {
		PutMappingRequestBuilder builder = elasticsearchClient.admin().indices().preparePutMapping(indexName);
		builder.setType(type.toString());
		builder.setSource(source);
		builder.execute().actionGet();
		logger.info("mapping done,type:{},source:{}", type, source);
	}

	@Override
	public void deleteByIndexName(String indexName2) {
		elasticsearchClient.admin().indices().prepareDelete(indexName2).execute().actionGet();
	}

	@Override
	public void createByIndexName(String indexName2) {
		elasticsearchClient.admin().indices().prepareCreate(indexName2).execute().actionGet();
	}

	@Override
	public boolean isExistByIndexName(String indexName2) {
		IndicesExistsResponse res = elasticsearchClient.admin().indices().prepareExists(indexName2).execute()
				.actionGet();
		return res.isExists();
	}
}
