package com.lanking.uxb.operation.index.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.search.api.IndexConfigService;
import com.lanking.uxb.service.search.api.IndexInfo;

/**
 * 索引管理相关接口
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月19日
 */
@RestController
@RequestMapping(value = "op/index")
public class OpIndexController {

	private Logger logger = LoggerFactory.getLogger(OpIndexController.class);

	@Autowired
	private IndexConfigService indexConfigService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "items" })
	public Value deleteIndex() {
		List<Map<String, Object>> infos = Lists.newArrayList();
		try {
			for (IndexTypeable type : IndexInfo.typeHandles.keySet()) {

				Map<String, Object> one = Maps.newHashMap();
				one.put("type", type.name());
				one.put("description", IndexInfo.typeHandles.get(type).getDescription());
				one.put("name", type + "(" + IndexInfo.typeHandles.get(type).getDescription() + ")");
				// one.put("dataCount",
				// IndexInfo.typeHandles.get(type).dataCount());
				one.put("indexCount", IndexInfo.typeHandles.get(type).indexCount());
				infos.add(one);
			}
			Collections.sort(infos, new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					String name1 = (String) m1.get("type");
					String name2 = (String) m2.get("type");
					return name1.compareTo(name2);
				}
			});
		} catch (Exception e) {
			return new Value(infos);
		}
		return new Value(infos);
	}

	/**
	 * 查看当前类型的数据数量
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "getRealDataCount" }, method = { RequestMethod.POST })
	public Value getRealDataCount(@RequestParam(value = "type") final IndexType type) {
		return new Value(IndexInfo.typeHandles.get(type).dataCount());
	}

	/**
	 * 根据type--reMapping
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "reMapping" })
	public Value reMapping(@RequestParam(value = "type") final IndexType type) {
		try {
			IndexInfo.typeHandles.get(type).reMapping();
		} catch (IOException e) {
			logger.error("remapping index error:", e);
			return new Value(new ServerException());
		}
		return new Value();
	}

	/**
	 * 指定页码或者继续重建
	 * 
	 * @param type
	 * @param startPage
	 *            开始页码
	 * @param endPage
	 *            结束页码
	 * @return
	 */
	@RequestMapping(value = { "continueReindex" })
	public Value continueReindex(@RequestParam(value = "type") final IndexType type,
			@RequestParam(value = "startPage", required = false) final Integer startPage,
			@RequestParam(value = "endPage", required = false) final Integer endPage) {
		/**
		 * 新增在原有基础上继续重建
		 */
		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (IndexInfo.typeHandles.get(type).accept(type)) {
					IndexInfo.typeHandles.get(type).continueReindex(startPage, endPage);
				}
			}
		});
		return new Value();
	}

	/**
	 * 重建索引
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "reindex" })
	public Value reindex(@RequestParam(value = "type") final IndexType type) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (IndexInfo.typeHandles.get(type).accept(type)) {
					IndexInfo.typeHandles.get(type).reindex();
				}
			}
		});
		return new Value();
	}

	/**
	 * 根据type清空索引
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "deleteByType" })
	public Value deleteByType(@RequestParam(value = "type") final IndexType type) {
		IndexInfo.typeHandles.get(type).deleteByQuery(type, null);
		return new Value();
	}

	/**
	 * 删除Index
	 * 
	 * @param indexName
	 * @return
	 */
	@RequestMapping(value = { "deleteByIndexName" })
	public Value deleteByIndexName(String indexName) {
		indexConfigService.deleteByIndexName(indexName);
		return new Value();
	}

	/**
	 * 创建Index
	 * 
	 * @param indexName
	 * @return
	 */
	@RequestMapping(value = { "createByIndexName" })
	public Value createByIndexName(String indexName) {
		if (!indexConfigService.isExistByIndexName(indexName)) {
			indexConfigService.createByIndexName(indexName);
		}
		return new Value();
	}
}
