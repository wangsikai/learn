package com.lanking.uxb.service.counter.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.CounterDetail;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface BizCounterProvider {
	Biz getBiz();

	Biz getOtherBiz();

	Counter getCounter(long bizId);

	Map<Long, Counter> getCounters(Collection<Long> bizIds);

	CounterDetail getCounterDetail(long bizId, long otherBizId);

	/**
	 * 根据OtherBizId集合获取批量的CounterDetail，返回的map key为otherBizId
	 * 
	 * @param bizId
	 * @param otherBizId
	 * @return
	 */
	Map<Long, CounterDetail> getCounterDetailsByOtherBizId(long bizId, Collection<Long> otherBizIds);

	/**
	 * 根据bizId集合获取批量的CounterDetail，返回的map key为bizId
	 * 
	 * @param bizId
	 * @param otherBizId
	 * @return
	 */
	Map<Long, CounterDetail> getCounterDetailsByBizId(Collection<Long> bizIds, long otherBizId);

	/**
	 * 根据OtherBizId获取批量的CounterDetail，返回的map key为bizId
	 * 
	 * @param bizId
	 * @param otherBizId
	 * @return
	 */
	Page<CounterDetail> queryCounterDetailsByOtherBizId(long otherBizId, Count count, Pageable pageable);

	Number getCount(long bizId, Count count);

	Map<Count, Long> getCounts(long bizId, Set<Count> count);

	void counter(long bizId, Count count, Number c);

	void counters(Count count, Map<Long, ? extends Number> cs);

	void counterReset(long bizId, Count count, Number c);

	void counterDetail(long bizId, long otherBizId, Count count, Number c);
}
