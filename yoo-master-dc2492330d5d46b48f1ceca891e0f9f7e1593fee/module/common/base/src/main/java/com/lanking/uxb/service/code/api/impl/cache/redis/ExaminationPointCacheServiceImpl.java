package com.lanking.uxb.service.code.api.impl.cache.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.cache.ExaminationPointCacheService;

/**
 * 考点Redis缓存服务
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("${common.code.cache}")
public class ExaminationPointCacheServiceImpl implements ExaminationPointService {
	@Autowired
	@Qualifier("ExaminationPointRepo")
	private Repo<ExaminationPoint, Long> repo;

	@Autowired
	private ExaminationPointCacheService examinationPointCacheService;

	@Override
	@SuppressWarnings("unchecked")
	public List<ExaminationPoint> mgetList(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}
		List<Long> notCachedPointIds = Lists.newArrayList();
		List<ExaminationPoint> points = new ArrayList<ExaminationPoint>(ids.size());
		for (Long id : ids) {
			ExaminationPoint point = examinationPointCacheService.get(id);
			if (point == null) {
				notCachedPointIds.add(id);
				continue;
			}

			if (point.getStatus() != Status.ENABLED) {
				continue;
			}

			points.add(point);
		}

		if (CollectionUtils.isNotEmpty(notCachedPointIds)) {
			List<ExaminationPoint> needCachePoints = repo.find("$findAll", Params.param("ids", notCachedPointIds))
					.list();

			points.addAll(needCachePoints);

			for (ExaminationPoint p : needCachePoints) {
				examinationPointCacheService.set(p);
			}
		}

		return points;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, ExaminationPoint> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}

		Map<Long, ExaminationPoint> retMap = new HashMap<Long, ExaminationPoint>(ids.size());
		List<Long> needCachedPointIds = Lists.newArrayList();
		for (Long id : ids) {
			ExaminationPoint point = examinationPointCacheService.get(id);
			if (point == null) {
				needCachedPointIds.add(id);
				continue;
			}

			if (point.getStatus() != Status.ENABLED) {
				continue;
			}

			retMap.put(point.getId(), point);
		}

		if (CollectionUtils.isNotEmpty(needCachedPointIds)) {
			List<ExaminationPoint> needCachePoints = repo.find("$findAll", Params.param("ids", ids)).list();

			for (ExaminationPoint p : needCachePoints) {
				retMap.put(p.getId(), p);

				examinationPointCacheService.set(p);
			}
		}
		return retMap;
	}

}
