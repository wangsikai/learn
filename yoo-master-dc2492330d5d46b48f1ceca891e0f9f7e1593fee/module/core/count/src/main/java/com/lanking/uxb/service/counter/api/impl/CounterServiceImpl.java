package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.CounterDetail;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.counter.api.CounterService;
import com.lanking.uxb.service.counter.cache.CounterCacheService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${counter.cache}")
class CounterServiceImpl implements CounterService {

	@Autowired
	@Qualifier("CounterRepo")
	private Repo<Counter, Long> counterRepo;
	@Autowired
	@Qualifier("CounterDetailRepo")
	private Repo<CounterDetail, Long> counterDetailRepo;

	@Autowired
	private CounterCacheService counterCacheService;

	@Override
	public Counter getCounter(Biz biz, long bizId) {
		return counterRepo.find("$getCounter", Params.param("biz", biz.getValue()).put("bizId", bizId)).get();
	}

	@Override
	public Map<Long, Counter> getCounters(Biz biz, Collection<Long> bizIds) {
		Map<Long, Counter> counterMap = Maps.newHashMap();
		if (null != bizIds && bizIds.size() > 0) {
			List<Counter> counters = counterRepo
					.find("$getCounters", Params.param("biz", biz.getValue()).put("bizIds", bizIds)).list();
			if (!CollectionUtils.isEmpty(counters)) {
				for (Counter counter : counters) {
					counterMap.put(counter.getBizId(), counter);
				}
			}
		}
		return counterMap;
	}

	@Override
	public CounterDetail getCounterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		return counterDetailRepo.find("$getCounterDetail", Params.param("biz", biz.getValue()).put("bizId", bizId)
				.put("otherBiz", otherBiz.getValue()).put("otherBizId", otherBizId)).get();
	}

	@Override
	public Map<Long, CounterDetail> getCounterDetailsByOtherBizId(Biz biz, long bizId, Biz otherBiz,
			Collection<Long> otherBizIds) {
		Map<Long, CounterDetail> map = new HashMap<Long, CounterDetail>();
		List<CounterDetail> details = counterDetailRepo
				.find("$getCounterDetailsByOtherBizId", Params.param("biz", biz.getValue()).put("bizId", bizId)
						.put("otherBiz", otherBiz.getValue()).put("otherBizIds", otherBizIds))
				.list();
		for (CounterDetail detail : details) {
			map.put(detail.getOtherBizId(), detail);
		}
		return map;
	}

	@Override
	public Map<Long, CounterDetail> getCounterDetailsByBizId(Biz biz, Collection<Long> bizIds, Biz otherBiz,
			long otherBizId) {
		Map<Long, CounterDetail> map = new HashMap<Long, CounterDetail>();
		List<CounterDetail> details = counterDetailRepo
				.find("$getCounterDetailsByBizId", Params.param("biz", biz.getValue()).put("bizIds", bizIds)
						.put("otherBiz", otherBiz.getValue()).put("otherBizId", otherBizId))
				.list();
		for (CounterDetail detail : details) {
			map.put(detail.getBizId(), detail);
		}
		return map;
	}

	@Override
	public Page<CounterDetail> queryCounterDetailsByOtherBizId(Biz biz, Biz otherBiz, long otherBizId, Count count,
			Pageable pageable) {
		Params params = Params.param("biz", biz.getValue());
		params.put("otherBiz", otherBiz.getValue());
		params.put("otherBizId", otherBizId);
		params.put("count", count);
		if (pageable.getOrders() != null && pageable.getOrders().size() > 0) {
			Order order = pageable.getOrders().iterator().next();
			params.put("orderColumn", order.getField());
			params.put("isAsc", order.isAsc());
		}
		return counterDetailRepo.find("$queryCounterDetailsByBizId", params).fetch(pageable);
	}

	@Override
	public Number getCount(Biz biz, long bizId, Count count) {
		return counterRepo
				.find("SELECT " + count.getValue() + " FROM counter WHERE biz = :biz AND biz_id = :bizId",
						Params.param("biz", biz.getValue()).put("bizId", bizId).put("cc", count.getValue()))
				.get(Number.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Count, Long> getCounts(Biz biz, long bizId, Set<Count> count) {
		String cc = "";
		for (Count c : count) {
			cc += c.getValue() + ",";
		}
		return counterRepo.find(
				"SELECT " + StringUtils.substringBeforeLast(cc, ",")
						+ " FROM counter WHERE biz = :biz AND biz_id = :bizId",
				Params.param("biz", biz.getValue()).put("bizId", bizId)).get(Map.class);
	}

	@Transactional
	@Override
	public void counter(Biz biz, long bizId, Count count, Number c) {
		if (!counterCacheService.existCounter(biz, bizId)) {
			Counter counter = getCounter(biz, bizId);
			if (counter == null) {
				counter = new Counter();
				counter.setBiz(biz.getValue());
				counter.setBizId(bizId);
				counter.setCreateAt(new Date());
				counter.setUpdateAt(counter.getCreateAt());
				counterRepo.save(counter);
			}
			counterCacheService.setExistCounter(biz, bizId);
		}
		String sql = "UPDATE counter SET " + count.getValue() + " =  " + count.getValue()
				+ "+ :c ,update_at = :updateAt WHERE biz = :biz AND biz_id = :bizId";
		counterRepo.execute(sql,
				Params.param("c", c).put("biz", biz.getValue()).put("bizId", bizId).put("updateAt", new Date()));
	}

	@Override
	@Transactional
	public void counters(Biz biz, Count count, Map<Long, ? extends Number> cs) {
		Set<Long> bizIds = cs.keySet();
		Map<Long, Counter> counters = counterCacheService.mget2(biz, bizIds);
		Set<Long> newBizIds = new HashSet<Long>();
		Set<Counter> newCounters = new HashSet<Counter>();
		Date date = new Date();
		for (Long bizId : bizIds) {
			if (counters.get(bizId) == null) {
				newBizIds.add(bizId);
				Counter counter = new Counter();
				counter.setBiz(biz.getValue());
				counter.setBizId(bizId);
				counter.setCreateAt(date);
				counter.setUpdateAt(counter.getCreateAt());
				newCounters.add(counter);
			}
		}
		if (newBizIds.size() > 0) {
			counterRepo.save(newCounters);
			counterCacheService.multiSetExistCounter(biz, bizIds);
		}

		for (Entry<Long, ? extends Number> entry : cs.entrySet()) {
			String sql = "UPDATE counter SET " + count.getValue() + " =  " + count.getValue()
					+ "+ :c ,update_at = :updateAt WHERE biz = :biz AND biz_id = :bizId";
			counterRepo.execute(sql, Params.param("c", entry.getValue()).put("biz", biz.getValue())
					.put("bizId", entry.getKey()).put("updateAt", new Date()));
		}
		counterCacheService.invalid(biz, bizIds);
	}

	@Transactional
	@Override
	public void counterReset(Biz biz, long bizId, Count count, Number c) {
		if (!counterCacheService.existCounter(biz, bizId)) {
			Counter counter = getCounter(biz, bizId);
			if (counter == null) {
				counter = new Counter();
				counter.setBiz(biz.getValue());
				counter.setBizId(bizId);
				counter.setCreateAt(new Date());
				counter.setUpdateAt(counter.getCreateAt());
				counterRepo.save(counter);
			}
			counterCacheService.setExistCounter(biz, bizId);
		}
		String sql = "UPDATE counter SET " + count.getValue()
				+ " = :c ,update_at = :updateAt WHERE biz = :biz AND biz_id = :bizId";
		counterRepo.execute(sql,
				Params.param("c", c).put("biz", biz.getValue()).put("bizId", bizId).put("updateAt", new Date()));
	}

	@Transactional
	@Override
	public void counterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId, Count count, Number c) {
		if (!counterCacheService.existCounterDetail(biz, bizId, otherBiz, otherBizId)) {
			CounterDetail counterDetail = getCounterDetail(biz, bizId, otherBiz, otherBizId);
			if (counterDetail == null) {
				counterDetail = new CounterDetail();
				counterDetail.setBiz(biz.getValue());
				counterDetail.setBizId(bizId);
				counterDetail.setOtherBiz(otherBiz.getValue());
				counterDetail.setOtherBizId(otherBizId);
				counterDetail.setCreateAt(new Date());
				counterDetailRepo.save(counterDetail);
			}
			counterCacheService.setExistCounterDetail(biz, bizId, otherBiz, otherBizId);
		}
		String sql = "UPDATE counter_detail SET " + count.getValue() + " =  " + count.getValue() + "+ :c ,"
				+ Count.getUpdateAt(count)
				+ " = :updateAt WHERE biz = :biz AND biz_id = :bizId AND other_biz = :otherBiz AND other_biz_id = :otherBizId";
		counterDetailRepo.execute(sql, Params.param("c", c).put("biz", biz.getValue()).put("bizId", bizId)
				.put("otherBiz", otherBiz.getValue()).put("otherBizId", otherBizId).put("updateAt", new Date()));
	}

}
