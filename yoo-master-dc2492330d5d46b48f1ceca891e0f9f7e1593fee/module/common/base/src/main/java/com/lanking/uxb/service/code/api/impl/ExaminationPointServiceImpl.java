package com.lanking.uxb.service.code.api.impl;

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

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.ExaminationPointService;

/**
 * 考点服务
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class ExaminationPointServiceImpl implements ExaminationPointService {
	@Autowired
	@Qualifier("ExaminationPointRepo")
	private Repo<ExaminationPoint, Long> repo;

	@Override
	@SuppressWarnings("unchecked")
	public List<ExaminationPoint> mgetList(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}
		return repo.find("$findAll", Params.param("ids", ids)).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, ExaminationPoint> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}

		List<ExaminationPoint> points = this.mgetList(ids);
		Map<Long, ExaminationPoint> retMap = new HashMap<Long, ExaminationPoint>(ids.size());
		for (ExaminationPoint e : points) {
			retMap.put(e.getId(), e);
		}
		return retMap;
	}
}
