package com.lanking.uxb.service.code.cache;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 考点缓存
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
public class ExaminationPointCacheService extends AbstractCacheService {
	private ValueOperations<String, ExaminationPoint> opsExamination;
	private static final String POINT_KEY = "p";

	private String getKey(long id) {
		return assemblyKey(POINT_KEY, id);
	}

	/**
	 * 获得缓存的知识点数据
	 *
	 * @param id
	 *            考点id
	 * @return {@link ExaminationPoint}
	 */
	public ExaminationPoint get(long id) {
		return opsExamination.get(getKey(id));
	}

	/**
	 * 缓存数据
	 *
	 * @param e
	 *            {@link ExaminationPoint}
	 */
	public void set(ExaminationPoint e) {
		opsExamination.set(getKey(e.getId()), e);
	}

	@Override
	public String getNs() {
		return "epoint-c";
	}

	@Override
	public String getNsCn() {
		return "考点列表缓存服务";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.opsExamination = getRedisTemplate().opsForValue();
	}
}
