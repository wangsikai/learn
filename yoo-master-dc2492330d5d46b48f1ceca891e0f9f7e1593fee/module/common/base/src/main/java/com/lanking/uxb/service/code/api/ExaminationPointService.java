package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;

/**
 * 考点相关服务
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface ExaminationPointService {
	/**
	 * 根据考点code列表查找数据
	 *
	 * @param ids
	 *            考点code列表
	 * @return {@link List}
	 */
	List<ExaminationPoint> mgetList(Collection<Long> ids);

	/**
	 * 根据考点code列表查找Map结构数据
	 *
	 * @param ids
	 *            考点code列表
	 * @return {@link Map}
	 */
	Map<Long, ExaminationPoint> mget(Collection<Long> ids);

}
