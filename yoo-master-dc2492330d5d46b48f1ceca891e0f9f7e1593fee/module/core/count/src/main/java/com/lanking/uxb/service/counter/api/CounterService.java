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

/**
 * 计数器基础API
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月10日
 *
 */
public interface CounterService {

	Counter getCounter(Biz biz, long bizId);

	Map<Long, Counter> getCounters(Biz biz, Collection<Long> bizIds);

	CounterDetail getCounterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId);

	/**
	 * 根据OtherBizId集合获取批量的CounterDetail，返回的map key为otherBizId
	 * 
	 * @param biz
	 * @param bizId
	 * @param otherBiz
	 * @param otherBizId
	 * @return
	 */
	Map<Long, CounterDetail> getCounterDetailsByOtherBizId(Biz biz, long bizId, Biz otherBiz,
			Collection<Long> otherBizIds);

	/**
	 * 根据bizId集合获取批量的CounterDetail，返回的map key为bizId
	 * 
	 * @param biz
	 * @param bizId
	 * @param otherBiz
	 * @param otherBizId
	 * @return
	 */
	Map<Long, CounterDetail> getCounterDetailsByBizId(Biz biz, Collection<Long> bizIds, Biz otherBiz, long otherBizId);

	/**
	 * 根据OtherBizId获取批量的CounterDetail，返回的map key为bizId
	 * 
	 * @param otherBizId
	 * @param pageable
	 * @return
	 */
	Page<CounterDetail> queryCounterDetailsByOtherBizId(Biz biz, Biz otherBiz, long otherBizId, Count count,
			Pageable pageable);

	Number getCount(Biz biz, long bizId, Count count);

	Map<Count, Long> getCounts(Biz biz, long bizId, Set<Count> count);

	void counter(Biz biz, long bizId, Count count, Number c);

	void counters(Biz biz, Count count, Map<Long, ? extends Number> cs);

	void counterReset(Biz biz, long bizId, Count count, Number c);

	void counterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId, Count count, Number c);
}
