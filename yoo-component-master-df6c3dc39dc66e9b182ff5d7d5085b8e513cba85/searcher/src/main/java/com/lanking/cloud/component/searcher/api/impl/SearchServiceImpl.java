package com.lanking.cloud.component.searcher.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.uxb.service.search.api.HighlightedConfig;
import com.lanking.uxb.service.search.api.Page;

@Service
public class SearchServiceImpl implements SearchService {

	@Value("${elasticsearch.index-name}")
	private String indexName;

	@Autowired
	private Client elasticsearchClient;

	@Override
	public Document get(IndexTypeable type, String id) {
		GetResponse response = elasticsearchClient.prepareGet(indexName, type.toString(), id).execute().actionGet();
		Document document = new Document(response.getType(), response.getId(), response.getSourceAsString());
		return document;
	}

	@Override
	public Page search(IndexTypeable type, int offset, int size, QueryBuilder qb, HighlightedConfig config,
			Order... orders) {
		return search(Lists.newArrayList(type), offset, size, qb, config, orders);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Page search(List<IndexTypeable> types, int offset, int size, QueryBuilder qb, HighlightedConfig config,
			Order... orders) {
		List<String> typeArr = new ArrayList<String>(types.size());
		for (IndexTypeable type : types) {
			typeArr.add(type.toString());
		}

		SearchRequestBuilder builder = elasticsearchClient.prepareSearch(indexName)
				.setTypes(typeArr.toArray(new String[typeArr.size()])).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb).setFrom(offset).setSize(size).setExplain(false);

		// 排序
		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					if ("_score".equals(order.getField())) {
						if (order.getDirection() == Direction.DESC) {
							builder.addSort(SortBuilders.scoreSort().order(SortOrder.DESC));
						} else {
							builder.addSort(SortBuilders.scoreSort().order(SortOrder.ASC));
						}
					} else {
						FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(order.getField());
						SortBuilder sortBuilder = fieldSortBuilder
								.order(SortOrder.valueOf(order.getDirection().getValue().toUpperCase()));
						builder.addSort(sortBuilder);
					}

				}
			}
		}
		SearchResponse response = builder.execute().actionGet();
		Page page = new Page();
		page.setTotalCount(response.getHits().getTotalHits());
		page.setPageSize(size);
		long tp = page.getTotalCount() % size;
		page.setTotalPage(tp == 0 ? page.getTotalCount() / size : (page.getTotalCount() / size + 1));
		for (SearchHit hit : response.getHits()) {
			JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
			Map<String, HighlightField> result = hit.highlightFields();
			if (result.size() > 0) {
				for (String fieldName : result.keySet()) {
					HighlightField titleField = result.get(fieldName);
					String value = "";
					for (Text text : titleField.fragments()) {
						value += text;
					}
					jsonObject.put(fieldName, value);
				}
			}
			Document document = new Document(hit.getType(), hit.getId(), jsonObject.toString(), hit.getScore());
			page.getDocuments().add(document);
		}
		return page;
	}

	@Override
	public long count(IndexTypeable type, QueryBuilder qb) {
		List<IndexTypeable> types = Lists.newArrayList(type);
		List<String> typeArr = new ArrayList<String>(types.size());
		for (IndexTypeable typeable : types) {
			typeArr.add(typeable.toString());
		}
		return elasticsearchClient.prepareSearch(indexName).setTypes(typeArr.toArray(new String[typeArr.size()]))
				.setQuery(qb).setSize(0).execute().actionGet().getHits().getTotalHits();
	}
}
