package com.lanking.uxb.service.search.api;

import java.util.Collection;

import org.elasticsearch.index.query.BoolQueryBuilder;

@SuppressWarnings("rawtypes")
public class QueryBuilders {

	public static BoolQueryBuilder inQuery(String name, Collection values) {
		BoolQueryBuilder qb = org.elasticsearch.index.query.QueryBuilders.boolQuery();
		for (Object value : values) {
			qb.should(org.elasticsearch.index.query.QueryBuilders.termQuery(name, value));
		}
		return qb;
	}

}
