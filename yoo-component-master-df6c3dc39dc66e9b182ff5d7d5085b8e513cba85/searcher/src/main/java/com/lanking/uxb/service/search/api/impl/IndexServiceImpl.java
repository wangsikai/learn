package com.lanking.uxb.service.search.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.uxb.service.search.api.IndexHandle;
import com.lanking.uxb.service.search.api.IndexInfo;
import com.lanking.uxb.service.search.api.IndexService;

@Service("indexService")
@Transactional(readOnly = true)
public class IndexServiceImpl implements IndexService, ApplicationContextAware, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

	private ApplicationContext appContext;

	private List<IndexHandle> indexHandles = Lists.newArrayList();

	@Autowired
	private Client elasticsearchClient;

	@Value("${elasticsearch.index-name}")
	private String indexName;

	@Async
	@Override
	public void add(IndexTypeable type, Long id) {
		this.syncAdd(type, id);
	}

	@Async
	@Override
	public void add(IndexTypeable type, Collection<Long> ids) {
		this.syncAdd(type, ids);
	}

	@Async
	@Override
	public void update(IndexTypeable type, Long id) {
		this.syncUpdate(type, id);
	}

	@Async
	@Override
	public void update(IndexTypeable type, Long id, Map<String, Object> fieldValues) {
		this.syncUpdate(type, id, fieldValues);
	}

	@Async
	@Override
	public void update(IndexTypeable type, Collection<Long> ids) {
		this.syncUpdate(type, ids);
	}

	@Async
	@Override
	public void delete(IndexTypeable type, Long id) {
		this.syncDelete(type, id);
	}

	@Async
	@Override
	public void delete(IndexTypeable type, Collection<Long> ids) {
		this.syncDelete(type, ids);
	}

	@Async
	@Override
	public void reindex(IndexTypeable type) {
		this.syncReindex(type);
	}

	@Async
	@Override
	public void deleteReindex(IndexTypeable type) {
		this.syncDeleteReindex(type);
	}

	@Override
	public void syncAdd(IndexTypeable type, Long id) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.add(id);
			}
		} catch (Exception e) {
			logger.error("add index error:", e);
		}
	}

	@Override
	public void syncAdd(IndexTypeable type, Collection<Long> ids) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.add(ids);
			}
		} catch (Exception e) {
			logger.error("add index error:", e);
		}
	}

	@Override
	public void syncUpdate(IndexTypeable type, Long id) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.update(id);
			}
		} catch (Exception e) {
			logger.error("update index error:", e);
		}
	}

	@Override
	public void syncUpdate(IndexTypeable type, Long id, Map<String, Object> fieldValues) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.update(id, fieldValues);
			}
		} catch (Exception e) {
			logger.error("update index error:", e);
		}
	}

	@Override
	public void syncUpdate(IndexTypeable type, Collection<Long> ids) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.update(ids);
			}
		} catch (Exception e) {
			logger.error("update index error:", e);
		}
	}

	@Override
	public void syncDelete(IndexTypeable type, Long id) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.delete(id);
			}
		} catch (Exception e) {
			logger.error("delete index error:", e);
		}
	}

	@Override
	public void syncDelete(IndexTypeable type, Collection<Long> ids) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.delete(ids);
			}
		} catch (Exception e) {
			logger.error("delete index error:", e);
		}
	}

	@Override
	public void syncReindex(IndexTypeable type) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.reindex();
			}
		} catch (Exception e) {
			logger.error("reindex error:", e);
		}
	}

	@Override
	public void syncDeleteReindex(IndexTypeable type) {
		try {
			IndexHandle handle = getIndexHandle(type);
			if (handle != null) {
				handle.deleteReindex();
			}
		} catch (Exception e) {
			logger.error("delete reindex error:", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (IndexHandle indexHandle : appContext.getBeansOfType(IndexHandle.class).values()) {
			logger.debug("{} bean is init", indexHandle.getClass().getName());
			indexHandles.add(indexHandle);
			IndexInfo.typeHandles.put(indexHandle.getType(), indexHandle);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

	private IndexHandle getIndexHandle(IndexTypeable type) {
		IndexHandle indexHandle = null;
		for (IndexHandle ih : indexHandles) {
			if (ih.accept(type)) {
				indexHandle = ih;
				break;
			}
		}
		return indexHandle;
	}
}
