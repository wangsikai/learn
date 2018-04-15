package com.lanking.uxb.service.search.api.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.uxb.service.search.api.IndexConfigService;
import com.lanking.uxb.service.search.api.IndexHandle;
import com.lanking.uxb.service.search.api.IndexInfo;

public abstract class AbstractIndexHandle extends IndexBuildServiceImpl implements IndexHandle {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IndexConfigService indexConfigService;
	@Autowired
	private SearchService searchService;

	@Override
	public boolean accept(IndexTypeable type) {
		return false;
	}

	@Override
	public IndexTypeable getType() {
		throw new NotImplementedException();
	}

	@Override
	public Document get(Long id) {
		throw new NotImplementedException();
	}

	@Override
	public List<Document> mget(Collection<Long> ids) {
		throw new NotImplementedException();
	}

	@Override
	public void add(Long id) {
		throw new NotImplementedException();
	}

	@Override
	public void add(Collection<Long> ids) {
		throw new NotImplementedException();

	}

	@Override
	public void update(Long id) {
		this.add(id);
	}

	@Override
	public void update(Long id, Map<String, Object> fieldValues) {
		throw new NotImplementedException();
	}

	@Override
	public void update(Collection<Long> ids) {
		this.add(ids);
	}

	@Override
	public void delete(Long id) {
		throw new NotImplementedException();
	}

	@Override
	public void delete(Collection<Long> ids) {
		for (Long bizId : ids) {
			this.delete(bizId);
		}
	}

	@Override
	public void reMapping() throws IOException {
		try {
			deleteByType(getType());
		} catch (Exception e) {
			logger.warn("delete by type error:", e);
		}
		indexConfigService.mapping(getType(), IndexInfo.typeScripts.get(getType()));
	}

	@Override
	public void reindex() {
		throw new NotImplementedException();
	}

	@Override
	public void deleteReindex() {
		throw new NotImplementedException();
	}

	@Override
	public long indexCount() {
		return searchService.count(this.getType(), QueryBuilders.matchAllQuery());
	}

}
