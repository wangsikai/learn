package com.lanking.cloud.component.searcher.api;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.uxb.service.search.api.HighlightedConfig;
import com.lanking.uxb.service.search.api.Page;

public interface SearchService {

	Document get(IndexTypeable type, String id);

	Page search(IndexTypeable type, int offset, int size, QueryBuilder qb, HighlightedConfig config, Order... orders);

	Page search(List<IndexTypeable> types, int offset, int size, QueryBuilder qb, HighlightedConfig config,
			Order... orders);

	long count(IndexTypeable type, QueryBuilder qb);

}
