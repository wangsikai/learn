package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.baseData.MetaKnowpointMap;

/**
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface ZyMetaKnowpointMapService {
	/**
	 * 根据父类查找子集
	 *
	 * @param code
	 *            知识点code
	 * @return {@link MetaKnowpointMap}
	 */
	List<MetaKnowpointMap> findByParent(int code);

	/**
	 * 根据多个父类查找知识点
	 *
	 * @param codes
	 *            知识点列表
	 * @return {@link MetaKnowpointMap}
	 */
	List<MetaKnowpointMap> findByParents(Collection<Integer> codes);
}
