package com.lanking.uxb.service.counter.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.CounterDetail;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class CounterCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;
	private ValueOperations<String, Counter> counterOpt;
	private ValueOperations<String, CounterDetail> counterDetailOpt;

	private static final String COUNTER_KEY = "nc";
	private static final String COUNTERDETAIL_KEY = "ncd";

	private static final String COUNTER_EXIST_KEY = "nc_e";
	private static final String COUNTERDETAIL_EXIST_KEY = "ncd_e";

	private String getCounterExistKey(Biz biz, long bizId) {
		return assemblyKey(COUNTER_EXIST_KEY, biz, bizId);
	}

	private String getCounterDetailExistKey(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		return assemblyKey(COUNTERDETAIL_EXIST_KEY, biz, bizId, otherBiz, otherBizId);
	}

	public void setExistCounter(Biz biz, long bizId) {
		stringOpt.set(getCounterExistKey(biz, bizId), "1");
	}

	public void multiSetExistCounter(Biz biz, Collection<Long> bizIds) {
		Map<String, String> map = new HashMap<String, String>(bizIds.size());
		for (Long bizId : bizIds) {
			map.put(getCounterExistKey(biz, bizId), "1");
		}
		stringOpt.multiSet(map);
	}

	public void setExistCounterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		stringOpt.set(getCounterDetailExistKey(biz, bizId, otherBiz, otherBizId), "1");
	}

	public boolean existCounter(Biz biz, long bizId) {
		return getRedisTemplate().hasKey(getCounterExistKey(biz, bizId));
	}

	public boolean existCounterDetail(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		return getRedisTemplate().hasKey(getCounterDetailExistKey(biz, bizId, otherBiz, otherBizId));
	}

	private String getCounterKey(Biz biz, long bizId) {
		return assemblyKey(COUNTER_KEY, biz, bizId);
	}

	private String getCounterDetailKey(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		return assemblyKey(COUNTERDETAIL_KEY, biz, bizId, otherBiz, otherBizId);
	}

	public void set(Counter counter) {
		counterOpt.set(getCounterKey(Biz.findByValue(counter.getBiz()), counter.getBizId()), counter);
	}

	public void set(CounterDetail counterDetail) {
		counterDetailOpt.set(
				getCounterDetailKey(Biz.findByValue(counterDetail.getBiz()), counterDetail.getBizId(),
						Biz.findByValue(counterDetail.getOtherBiz()), counterDetail.getOtherBizId()), counterDetail);
	}

	public Counter get(Biz biz, long bizId) {
		return counterOpt.get(getCounterKey(biz, bizId));
	}

	public CounterDetail get(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		return counterDetailOpt.get(getCounterDetailKey(biz, bizId, otherBiz, otherBizId));
	}

	public void invalid(Biz biz, long bizId) {
		getRedisTemplate().delete(getCounterKey(biz, bizId));
	}

	public void invalid(Biz biz, long bizId, Biz otherBiz, long otherBizId) {
		getRedisTemplate().delete(getCounterDetailKey(biz, bizId, otherBiz, otherBizId));
	}

	public void invalid(Biz biz, Collection<Long> bizIds) {
		if (bizIds == null || bizIds.size() == 0) {
			return;
		}
		Set<String> keys = new HashSet<String>(bizIds.size());
		for (Long bizId : bizIds) {
			keys.add(getCounterKey(biz, bizId));
		}
		getRedisTemplate().delete(keys);
	}

	public List<Counter> mget(Biz biz, Collection<Long> bizIds) {
		List<String> keys = Lists.newArrayList();
		for (long bizId : bizIds) {
			keys.add(getCounterKey(biz, bizId));
		}
		List<Counter> counters = counterOpt.multiGet(keys);
		return CollectionUtils.trimNull(counters);
	}

	/**
	 * 批量获取counter
	 * 
	 * @param biz
	 * @param bizIds
	 * @return key: bizId, value: Counter
	 */
	public Map<Long, Counter> mget2(Biz biz, Collection<Long> bizIds) {
		List<String> keys = Lists.newArrayList();
		for (long bizId : bizIds) {
			keys.add(getCounterKey(biz, bizId));
		}
		List<Counter> counters = counterOpt.multiGet(keys);
		Map<Long, Counter> map = new HashMap<Long, Counter>();
		for (Counter counter : counters) {
			if (null != counter) {
				map.put(counter.getBizId(), counter);
			}
		}
		return map;
	}

	public List<CounterDetail> mget(Biz biz, Collection<Long> bizIds, Biz otherBiz, long otherBizId) {
		List<String> keys = Lists.newArrayList();
		for (long bizId : bizIds) {
			keys.add(getCounterDetailKey(biz, bizId, otherBiz, otherBizId));
		}
		List<CounterDetail> counterDetails = counterDetailOpt.multiGet(keys);
		return CollectionUtils.trimNull(counterDetails);
	}

	public List<CounterDetail> mget(Biz biz, long bizId, Biz otherBiz, Collection<Long> otherBizIds) {
		List<String> keys = Lists.newArrayList();
		for (long otherBizId : otherBizIds) {
			keys.add(getCounterDetailKey(biz, bizId, otherBiz, otherBizId));
		}
		List<CounterDetail> counterDetails = counterDetailOpt.multiGet(keys);
		return CollectionUtils.trimNull(counterDetails);
	}

	@Override
	public String getNs() {
		return "c";
	}

	@Override
	public String getNsCn() {
		return "计数";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
		counterOpt = getRedisTemplate().opsForValue();
		counterDetailOpt = getRedisTemplate().opsForValue();
	}

}
